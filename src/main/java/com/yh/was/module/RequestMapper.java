package com.yh.was.module;

import com.yh.was.interfaces.IServlet;
import com.yh.was.launcher.Launcher;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.www.protocol.file.FileURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**
 * com.yh.was.service패키지에 소속된 IServlet 구현체를 mapping하였습니다.
 * 추후 urlConfigs를 읽어들여와 putServlet 메서드를 할때 비교를 해주어 교체를 해주어야합니다.
 *
 */
public class RequestMapper {
    private static final Logger logger = LoggerFactory.getLogger(RequestMapper.class);
    private Map<String, IServlet> serviceMap;
    private String packageName;
    private Class<?> filter;
    // TODO url mapping을 위한 Map
    private Map<String, String> urlConfigs;
    private static RequestMapper instance;
    public synchronized static RequestMapper getInstance() {
        if (instance == null) {
            instance = new RequestMapper();
        }
        return instance;
    }

    public RequestMapper() {
        // TODO 설정 파일
        serviceMap = new HashMap<String, IServlet>();
        packageName = "com.yh.was.service";
        filter = IServlet.class;
    }

    public IServlet getServlet(String requestName) {
        return serviceMap.get(requestName.toUpperCase());
    }

    public void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            if (classLoader == null) {
                throw new ClassNotFoundException("Failed to get the class loader.");
            }
            Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                URLConnection urlConnection = url.openConnection();
                if (urlConnection instanceof FileURLConnection) {
                    File packageDict = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
                    findMapperForDict(packageDict, url, urlConnection, packageName);
                } else if (urlConnection instanceof JarURLConnection)  {
                    findMapperForJar(urlConnection, packageName);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | IOException ex) {
            logger.error("", ex);
        }
    }

    private void findMapperForDict(File dict, URL url, URLConnection urlConnection, String amountPackageName)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, UnsupportedEncodingException {
        File tempDirectory = null;
        String[] files = dict.list();
        for (String file : files) {
            if (file.endsWith(".class")) {
                Class<?> classObject = Class.forName(amountPackageName + '.' + file.substring(0, file.length() - 6));
                String servletName = (amountPackageName + "." + classObject.getSimpleName())
                        .replace(packageName + ".", "").toUpperCase();
                List<Class<?>> interfaceList = ClassUtils.getAllInterfaces(classObject);
                loopPutServlet(interfaceList, filter, classObject, servletName);

                List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(classObject);
                loopPutServlet(superclassList, filter, classObject, servletName);
            } else if ((tempDirectory = new File(dict, file)).isDirectory()) {
                findMapperForDict(tempDirectory, url, urlConnection, amountPackageName + "." + file);
            }
        }
    }

    private void findMapperForJar(URLConnection urlConnection, String amountPackageName)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        JarFile jarFile = ((JarURLConnection)urlConnection).getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        for (JarEntry jarEntry = null; entries.hasMoreElements() && ((jarEntry = entries.nextElement()) != null);) {
            String name = jarEntry.getName();
            if (!name.contains(".class"))
                continue;
            name = name.substring(0, name.length() - 6).replace('/', '.');
            if (name.contains(amountPackageName)) {
                Class<?> classObject = Class.forName(name);
                if (filter.equals(classObject)) {
                    putServlet(classObject, name.replace(packageName + ".","").toUpperCase());
                    break;
                } else {
                    List<Class<?>> interfaceList = ClassUtils.getAllInterfaces(classObject);
                    loopPutServlet(interfaceList, filter, classObject
                            , name.replace(packageName + ".","").toUpperCase());

                    List<Class<?>> superclassList = ClassUtils.getAllSuperclasses(classObject);
                    loopPutServlet(superclassList, filter, classObject
                            , name.replace(packageName + ".","").toUpperCase());
                }
            }
        }
    }

    private void loopPutServlet(List<Class<?>> classList, Class<?> filter, Class<?> classObject, String name)
            throws InstantiationException, IllegalAccessException {
        for (Class<?> classItem : classList) {
            if (classItem.equals(filter)) {

                putServlet(classObject, name);
                break;
            }
        }
    }

    private void putServlet(Class<?> classObject, String name) throws IllegalAccessException, InstantiationException {
        IServlet servlet = (IServlet) classObject.newInstance();
        /** TODO {"SERVICE.HELLO":ISerVlet},,, 형태로 저장중
         * String url = urlConfigs.get(name);        설정 파일에서 서블렛 패키지 경로에 해당하는 url 획득
         * serviceMap.put(url, servlet);             패키지 경로 대신 url로 변경
         **/
        serviceMap.put(name, servlet);
        logger.info("Added servlet by class: " + name);
    }
}
