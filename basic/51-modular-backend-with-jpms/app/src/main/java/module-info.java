module com.example.jpms.app {
    requires com.example.jpms.web;
    requires com.example.jpms.service;
    
    uses com.example.jpms.service.ProductService;
}
