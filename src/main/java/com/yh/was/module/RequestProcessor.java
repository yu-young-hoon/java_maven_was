package com.yh.was.module;

import com.yh.was.config.Host;
import com.yh.was.error.ForbiddenErrorException;
import com.yh.was.error.HttpServerException;
import com.yh.was.error.NotFoundErrorException;
import com.yh.was.error.RequestFactoryErrorException;
import com.yh.was.interfaces.IServlet;
import com.yh.was.response.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Requset를 처리하는 스레드입니다.
 * 스레드에서 발생하는 에러를 기록합니다.
 * 해당하는 hostname이 없을 경우 같은 포트의 첫번째 호스트로 에러를 출력합니다.
 * path name이 없다면 index.html을 출력합니다.
 *
 */
public class RequestProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestProcessor.class);
    private Socket connection;
    private List<Host> hosts;
    Host vhost;

    public RequestProcessor(Socket request, List<Host> hosts) {
        this.connection = request;
        this.hosts = hosts;
    }

    public void run() {
        HttpResponse response = null;
        vhost = hosts.get(0);;
        try {
            logger.info("Request processing start", connection);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
            response = HttpResponseFactory.getInstance().create(out);

            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
            HttpRequest request = HttpRequestFactory.getInstance().create(in);
            List<Host> vhosts = hosts.stream().filter(host-> host.getHost().equals(request.getRemoteAddress())).collect(Collectors.toList());
            if (vhosts.size() == 0) {
                // 해당하는 호스트가 없다면 첫번째 호스트로 에러표시
                throw new ForbiddenErrorException("Not match host with port");
            }
            vhost = vhosts.get(0);

            IServlet servlet = RequestMapper.getInstance().getServlet(request.getRequestName());
            if (servlet != null) {
                // 서블릿이 있을 경우
                if(request.getMethod() == HttpRequest.METHOD.GET) {
                    servlet.get(request, response);
                } else if (request.getMethod() == HttpRequest.METHOD.GET) {
                    servlet.post(request, response);
                } else {
                    // TODO another method
                }
            } else {
                // static 파일일 경우
                String requestName = request.getRequestName();

                // 상위 폴더 혹은 실행파일 접근
                if (requestName.contains("..") || requestName.contains(".exe")) {
                    throw new ForbiddenErrorException("forbidden access");
                }

                // 해당하는 파일이 없을 경우
                Path path = Paths.get(vhost.getDocumentRoot().toString() + "/" + requestName);
                if (!Files.exists(path)) {
                    throw new NotFoundErrorException("Static file not found");
                }
                // path name이 없을 경우
                if (requestName.isEmpty())
                    writeFileForResponse(response, Paths.get(vhost.getDocumentRoot().toString() + "/" + "index.html"));
                else // 파일이 있을 경우
                    writeFileForResponse(response, path);
            }
        } catch (ForbiddenErrorException ex) {
            response.setStatus(ResponseStatus.Forbidden);
            logger.error("Request forbidden access error", ex);
        } catch (NotFoundErrorException ex) {
            response.setStatus(ResponseStatus.NotFound);
            logger.error("Request not found static file", ex);
        } catch (UnsupportedEncodingException ex) {
            response.setStatus(ResponseStatus.InternalServerError);
            logger.error("Request socket encoding error", ex);
        } catch (IOException ex) {
            response.setStatus(ResponseStatus.InternalServerError);
            logger.error("Request socket io error", ex);
        }  catch (RequestFactoryErrorException ex) {
            response.setStatus(ResponseStatus.InternalServerError);
            logger.error("Request create fail", ex);
        } catch (Exception ex) {
            response.setStatus(ResponseStatus.InternalServerError);
            logger.error("Request fail", ex);
        } finally {
            try {
                if(response != null) {
                    // status를 확인하고 에러가 있다면 에러 페이지에 해당하는 파일로 메시지를 교체합니다.
                    writeFileForResponseByCode(response);
                    // 헤더와 메시지를 전송합니다.
                    response.sendAll();
                }
                connection.close();
            } catch (IOException ex) {
                logger.error("Request socket close error", ex);
            }
        }
    }
    private void writeFileForResponseByCode(HttpResponse res) throws IOException {
        ResponseStatus status = res.getStatus();
        if(status == ResponseStatus.Forbidden) {
            writeFileForResponse(res, Paths.get(vhost.getDocumentRoot().toString() + "/" + vhost.getErrorDocument(ResponseStatus.Forbidden)));
        } else if (status == ResponseStatus.NotFound) {
            writeFileForResponse(res, Paths.get(vhost.getDocumentRoot().toString() + "/" + vhost.getErrorDocument(ResponseStatus.NotFound)));
        } else if (status == ResponseStatus.InternalServerError) {
            writeFileForResponse(res, Paths.get(vhost.getDocumentRoot().toString() + "/" + vhost.getErrorDocument(ResponseStatus.InternalServerError)));
        } else {
            // TODO annother error
        }

    }
    private void writeFileForResponse(HttpResponse res, Path path) throws IOException {
        Files.readAllLines(path).stream().forEachOrdered(l->{
            res.addTextLine(l);
        });
    }
}