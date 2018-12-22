package com.tutorialspoint.demo.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tutorialspoint.demo.demo.exception.BookNotfoundException;
import com.tutorialspoint.demo.demo.model.Book;
import com.tutorialspoint.demo.demo.model.BookWrapper;
import com.tutorialspoint.demo.demo.service.ProductService;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RefreshScope
@RestController
public class RestEndpointController {

	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ProductService productService;
	
	@Value("${server.port}")
	private String port;
	
	//leido del servidor de Spring Cloud Config!!
    @Value("${welcome.message}")
    String welcomeText;
    
    
	
	private static final String URL_API_BOOKS =
            "http://private-114e-booksapi.apiary-mock.com/books/";
	
	//private static Map<Long, Book> bookRepo = new HashMap<>();

//	@Bean
//	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
//		return builder.build();
//	}
	
	@RequestMapping(value = "/welcome")
	@GetMapping
	public String welcomeText() {
		return welcomeText;
	}

	@RequestMapping(value="/")
	public String hello() {
	return "Hello World";
	}
	
	
/*
	private void addSelfLink(BookWrapper resource){
	    final ResponseEntity<Book> book = methodOn(RestEndpointController.class).getOneBook(resource.getBookid()+"");
	    final Link link = linkTo(book).withSelfRel();
	    resource.add(link);
	}
*/
	
	
	
	//También puedo devolver directamente un objeto con el @ResponseBody
	@RequestMapping(value="/book2")
	@GetMapping
	public @ResponseBody Book getProduct2(@RequestHeader("Accept") String accept) {
		System.out.println("Llamada devuelve uno cualquiera");
		System.out.println("accept " + accept);
		    Book book = new Book();
		    book.setTitle("book test!");
			return book;
	}
	
	
	//T..pero lo natural sera devolver ResponseEntities
	//Le puedo decir que admita peticiones en XML o en JSON.
	//Eso vendrá en el header de la request: Accept: application/xml, por ejemplo
	//El header nos indica qué es lo que quiere recibir.
	//Para esto hay que meter jackson as dependency --> NECESARIO
	//HAY que indicar el value en la peticion si lo hacemos asi
	@RequestMapping(value="/book", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
	@GetMapping
	public ResponseEntity<Object> getProduct(@RequestHeader("Accept") String accept) {
		System.out.println("Llamada devuelve uno cualquiera");
		System.out.println("accept " + accept);
		    Book book = new Book();
		    book.setTitle("book test!");
		    //cómo retornar HEADERS
		    HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            
			return new ResponseEntity<>(book, HttpStatus.OK);
	}
	 
	//POST: Para crear un resource. NO es idempotente.
	@RequestMapping(value="/books", method=RequestMethod.POST)
	//@PostMapping
	public ResponseEntity<Object> createProduct(@RequestBody Book book)
	{
		System.out.println("Llamada createProduct");
		productService.createProduct(book);
		return new ResponseEntity<>("Book is created successfully", HttpStatus.CREATED);
	}
	
	//PUT: Para editar un resource. Es idempotente.
	//se pasa el parametro id como parte de la url y se recoge como pathvariable
	
	@RequestMapping(value="/books/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateBook(@PathVariable("id") String id, @RequestBody Book book) {
		System.out.println("Llamada updateProduct");
		
		productService.updateProduct(new Long(id), book);
		return new ResponseEntity<>("Book is updated successsfully", HttpStatus.OK);
	}
	
	//DELETE
	
	@RequestMapping(value="/books/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> delete(@PathVariable("id") String id) {
		
		System.out.println("Llamada delete");
		productService.deleteProduct(new Long(id));
		return new ResponseEntity<>("Book has been deleted successsfully", HttpStatus.OK);
	}
	
	//Get one book
	
	@RequestMapping(value="/books/{id}", method=RequestMethod.GET)
	public ResponseEntity<Book> getOneBook(@PathVariable("id") String id) {
		
		if(!productService.containsKey(new Long(id)))
			throw new BookNotfoundException();
		
		
		Book book = productService.getProduct(new Long(id));
		
		System.out.println("Llamada getOneBook "+ book.getTitle() +", " + book.getId()   );
		return new ResponseEntity<Book>(book, HttpStatus.OK);
	}
	
	//Saca lista completa
	
	@RequestMapping(value="/AllMyBooks") 
	@GetMapping
	public ResponseEntity<Object>  getAllMyBooks() {
		
		System.out.println("Llamada allMyBooks ");
		
		return new ResponseEntity<>(productService.getProducts() , HttpStatus.OK);
		
		
	}
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException {
		File convertFile = new File("/var/tmp/" + file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		return "File is upload successfully";
	}
				
	
	
	
	
	/////////////////////////////////
	//
	//		TEST APIARY
	//
	/////////////////////////////////
	
	@RequestMapping(value="/apiaryBooks", method=RequestMethod.GET) 
	public ResponseEntity<Book[]>  apiaryGetAll() {
		ResponseEntity<Book[]> response =
	            restTemplate.getForEntity(URL_API_BOOKS, Book[].class);
		System.out.println();
		System.out.println("Llamada apiaryGetAll ");
		System.out.println("GET All StatusCode = " + response.getStatusCode());
		System.out.println("GET All Headers = " + response.getHeaders());
		System.out.println("GET Body (object list): ");
		Arrays.asList(response.getBody())
		            .forEach(book -> System.out.println("--> " + book));
		response.status(HttpStatus.OK);
		
	    return response;
	}
	
	
	@RequestMapping(value="/apiaryBooks/{id}", method=RequestMethod.GET) 
	public ResponseEntity<Book>  apiaryGetOne(@PathVariable("id") String id) {
		//ResponseEntity<Book[]> response =  getRestTemplate().getForEntity(URL_API_BOOKS, Book[].class, id);
		ResponseEntity<Book> response = restTemplate.getForEntity(URL_API_BOOKS+"/"+id,Book.class);
		System.out.println();
		System.out.println("Llamada apiaryGetOne ");
		System.out.println("GET All StatusCode = " + response.getStatusCode());
		System.out.println("GET All Headers = " + response.getHeaders());
		System.out.println("GET Body (object ): "+ response.getBody());
		
		response.status(HttpStatus.OK);
		System.out.println("volviendooo ");
	   return response;
	}
	
	
	
	@RequestMapping(value="/apiaryBooks", method=RequestMethod.POST)
	//@PostMapping
	public ResponseEntity<Object> createProductApiary()
	{
		System.out.println("Llamada createProductApiary");
	    Book book = new Book();
	    book.setTitle("book test!");
	    book.setId(99L);
		ResponseEntity<Book> response =
	            restTemplate.postForEntity(URL_API_BOOKS, book, Book.class);
		
		return new ResponseEntity<>("Book is created successfully", HttpStatus.CREATED);
	}
	
	
	//prueba con exchange y headers
	@RequestMapping(value = "/template/books")
	@GetMapping
	public String getProductList() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		System.out.println("port" + port);
		return restTemplate.exchange("http://localhost:9090/book", HttpMethod.GET, entity, String.class).getBody();
	}
	
	@RequestMapping(value = "/template/products")
	@PostMapping
	public String createProducts(@RequestBody Book book) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Book> entity = new HttpEntity<Book>(book, headers);
		return restTemplate.exchange("http://localhost:9090/books", HttpMethod.POST, entity, String.class).getBody();
	}
	
	
	//importante: Si uso @PutMapping, @DeleteMapping, ... pero el @RequestMapping value =... coincide, entonces
	//da un error. Las notaciones Mapping sólo pueden usarse si el value no se repite para diferentes requestMapppings
	
	@RequestMapping(value = "/template/products/{id}", method=RequestMethod.PUT)
	public String updateProduct(@PathVariable("id") String id, @RequestBody Book product) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		System.out.println("port" + port);
		HttpEntity<Book> entity = new HttpEntity<Book>(product, headers);
		return restTemplate.exchange("http://localhost:"+port+"/books/" + id, HttpMethod.PUT, entity, String.class)
				.getBody();
	}
	
	
	@RequestMapping(value = "/template/products/{id}", method=RequestMethod.DELETE)
	public String deleteProduct(@PathVariable("id") String id) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Book> entity = new HttpEntity<Book>(headers);
		System.out.println("port" + port);
		return restTemplate.exchange("http://localhost:"+port+"/books/" + id, HttpMethod.DELETE, entity, String.class)
				.getBody();
	}
	
	

	
}
