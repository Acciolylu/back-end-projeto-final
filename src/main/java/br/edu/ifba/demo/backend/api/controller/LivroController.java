package br.edu.ifba.demo.backend.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.edu.ifba.demo.backend.api.repository.LivroRepository;
import br.edu.ifba.demo.backend.api.dto.LivroDTO;
import br.edu.ifba.demo.backend.api.model.LivroModel;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/livros")

public class LivroController {

    private LivroRepository livRepository;
	
	public LivroController(LivroRepository livRepository) {
		super();
		this.livRepository = livRepository;
	}

    // método para cadastrar  livro
    @PostMapping()
    public ResponseEntity<LivroModel> cadastrarLivro(@RequestBody @Validated LivroModel livroModel) {
        try {
            LivroModel livroSalvo = livRepository.save(livroModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
	public String teste() {
		return "Testando Rota livro";
	}

    
	// metodo listar todos
	@GetMapping("/listall")
	public List<LivroModel> listall() {
		var livros = livRepository.findAll();
		return livros;
	}

    // metodo buscar id
	
    @GetMapping("/{id}")
    public LivroModel findById(@PathVariable("id") Long id) {
		Optional<LivroModel> livro = livRepository.findById(id);
		if ( livro.isPresent() )
			return livro.get();
        return null; //errado
    }

     // metodo buscar titulo
     
	@GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroDTO>> buscarTitulo(@PathVariable String titulo) {
		var livros= livRepository.findByTituloContainingIgnoreCase(titulo);
       
		if ( livros.isEmpty() ){
            return ResponseEntity.notFound().build();
        }
		return ResponseEntity.ok(LivroDTO.converter(livros));
       
    }

     // metodo buscar isbn
     
     @GetMapping("/isbn/{isbn}")
     public ResponseEntity<LivroDTO> buscarIsbn(@PathVariable String isbn) {
         var livro = livRepository.findByIsbn(isbn);
     
         if (livro == null) {
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(LivroDTO.converter(livro));
         

}

@DeleteMapping("/{id}")
public ResponseEntity<?> deletarLivro(@PathVariable Long id) {
    try {
        // verifica se o livro existe
        Optional<LivroModel> livro = livRepository.findById(id);
        if (livro.isPresent()) {
            livRepository.deleteById(id); // deleta
            return ResponseEntity.ok("Livro deletado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar o livro.");
    }
}

}

