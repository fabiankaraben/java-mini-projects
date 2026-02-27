module com.example.jpms.web {
    requires jdk.httpserver;
    requires com.example.jpms.service;
    
    exports com.example.jpms.web;
}
