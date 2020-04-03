-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=com.ibm.websphere.appserver.vertxCore-1.0
visibility=private
-bundles=\
 com.ibm.ws.io.netty.buffer,\
 com.ibm.ws.io.netty.codec,\
 com.ibm.ws.io.netty.codec-dns,\
 com.ibm.ws.io.netty.codec-http,\
 com.ibm.ws.io.netty.codec-http2,\
 com.ibm.ws.io.netty.codec-socks,\
 com.ibm.ws.io.netty.common,\
 com.ibm.ws.io.netty.handler,\
 com.ibm.ws.io.netty.handler-proxy,\
 com.ibm.ws.io.netty.resolver,\
 com.ibm.ws.io.netty.resolver-dns,\
 com.ibm.ws.io.netty.transport,\
 com.ibm.ws.io.netty.transport-native-epoll,\
 com.ibm.ws.io.netty.transport-native-kqueue,\
 com.ibm.ws.io.netty.transport-native-unix-common,\
 com.ibm.ws.io.vertx.core,\
 com.ibm.ws.io.vertx.codegen,\
 com.ibm.ws.org.mvel2,\
 com.ibm.ws.com.fasterxml.jackson.2.9.1
kind=ga
edition=base
WLP-Activation-Type: parallel
