package application.controller;
 
import java.util.Optional;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
 
import application.model.Genero;
import application.model.Jogos;
import application.repository.GeneroRepository;
import application.repository.JogosRepository;
 
@Controller
@RequestMapping(value = {"/", "/jogos"})
public class LivroController {
    @Autowired
    private JogosRepository jogosRepo;
    @Autowired
    private GeneroRepository generosRepo;
 
    @RequestMapping(value = {"", "/list"})
    public String list(Model ui){
        ui.addAttribute("jogos", jogosRepo.findAll());
        return "/jogo/list";
    }
 
    @RequestMapping("/insert")
    public String insert(Model ui) {
        ui.addAttribute("generos", generosRepo.findAll());
        return "/jogo/insert";
    }
 
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(
        @RequestParam("titulo") String titulo,
        @RequestParam("genero") long generoId) {
           
        Optional<Genero> resultado = generosRepo.findById(generoId);
        if(resultado.isPresent()) {
            Jogos jogos = new Jogos();
            jogos.setTitulo(titulo);
            jogos.setGenero(resultado.get());
 
            jogosRepo.save(jogos);
        }
       
        return "redirect:/jogos/list";
    }
 
    @RequestMapping("/update")
    public String update(@RequestParam("id") long id, Model ui) {
        Optional<Jogos> result = jogosRepo.findById(id);
        if(result.isPresent()) {
            ui.addAttribute("jogos", result.get());
            ui.addAttribute("generos", generosRepo.findAll());
            return "/jogos/update";
        }
        return "redirect:/jogos/list";
    }
 
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@RequestParam("id") long id, @RequestParam("titulo") String titulo, @RequestParam("genero") long generoId) {
        Optional<Jogos> result = jogosRepo.findById(id);
        if(result.isPresent()) {
            Optional<Genero> resultGenero = generosRepo.findById(generoId);
            if(resultGenero.isPresent()) {
                result.get().setTitulo(titulo);
                result.get().setGenero(resultGenero.get());
 
                jogosRepo.save(result.get());
            }
        }
 
        return "redirect:/jogos/list";
    }
 
    @RequestMapping("/delete")
    public String delete(Model ui, @RequestParam("id") long id) {
        Optional<Jogos> resultado = jogosRepo.findById(id);
 
        if(resultado.isPresent()) {
            ui.addAttribute("jogos", resultado.get());
            return "/jogos/delete";    
        }
 
        return "redirect:/jogos/list";
    }
 
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("id") long id) {
        jogosRepo.deleteById(id);
 
        return "redirect:/jogos/list";
    }
 
}
 