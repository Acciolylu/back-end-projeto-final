package br.edu.ifba.demo.backend.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifba.demo.backend.api.model.LivroModel;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<LivroModel, Long>{

     LivroModel findByIsbn(String isbn);

     List<LivroModel> findByTituloContainingIgnoreCase(String titulo);

    
}