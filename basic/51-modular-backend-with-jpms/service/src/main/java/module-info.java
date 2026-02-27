module com.example.jpms.service {
    requires transitive com.example.jpms.model;
    exports com.example.jpms.service;
    provides com.example.jpms.service.ProductService with com.example.jpms.service.impl.ProductServiceImpl;
}
