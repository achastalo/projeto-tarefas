package br.com.caelum.tarefas.controller;

import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.caelum.tarefas.dao.JdbcTarefaDao;
import br.com.caelum.tarefas.modelo.Tarefa;

/**
 * A anotação @Controller indica ao Spring que os métodos dessa classe são ações
 * (Action).
 * 
 * Podemos criar um método de qualquer nome dentro dessa classe, desde que ele
 * esteja com a anotação @RequestMapping . A anotação @RequestMapping recebe um
 * atributo chamado value que indica qual será a URL utilizada para invocar o
 * método, como esse atributo já é o padrão não precisamos definir. Portanto, se
 * colocarmos o valor olaMundoSpring acessaremos o método dentro do
 * nosso @Controller pela URL http://localhost:8080/fj21-tarefas/olaMundoSpring
 * 
 * Dentro dessa nossa classe TarefasController em nenhum momento temos um
 * HttpServletRequest para pegarmos os parâmetros enviados na requisição e
 * montarmos o objeto tarefa . Uma das grandes vantagens de frameworks modernos
 * é que eles conseguem popular os objetos para nós. Basta que de alguma forma,
 * nós façamos uma ligação entre o campo que está na tela com o objeto que
 * queremos popular. E com o Spring MVC não é diferente. Essa ligação é feita
 * através da criação de um parâmetro do método adiciona . Esse parâmetro é o
 * objeto que deverá ser populado pelo Spring MVC com os dados que vieram da
 * requisição.
 */

@Controller
public class TarefasController {

	private final JdbcTarefaDao dao;
	
	@Autowired
	public TarefasController(JdbcTarefaDao dao) {
		this.dao = dao;
	}
	
	@RequestMapping("adicionaTarefa")
	public String adiciona(@Valid Tarefa tarefa, BindingResult result) throws SQLException {

		if (result.hasFieldErrors("descricao")) {
			return "tarefa/formulario";
		}

		dao.adiciona(tarefa);
		return "tarefa/adicionada";
	}

	/**
	 * É preciso carregar o JSP no navegador, mas o acesso direto a pasta WEB-INF é
	 * proibido pelo servlet-container e consequentemente não é possível acessar o
	 * formulário. Para resolver isso vamos criar uma nova ação que tem a finalidade
	 * de chamar o formulário apenas
	 */
	@RequestMapping("novaTarefa")
	public String form() {
		return "tarefa/formulario";
	}

	/**
	 * Os dados para a exibição na tela e o nome da página JSP são encapsulados pelo
	 * Spring MVC em uma classe chamada ModelAndView
	 */
	@RequestMapping("listaTarefas")
	public String lista(Model model) throws SQLException {
		model.addAttribute("tarefas", dao.lista());
		return "tarefa/lista";
	}

	/**
	 * Para fazer um redirecionamento no lado do servidor basta usar o prefixo
	 * forward no retorno: Para fazer um redirecionamento no lado do cliente usamos
	 * o prefixo redirect:
	 */
	@RequestMapping("removeTarefa")
	public String remove(Tarefa tarefa) throws SQLException {
		dao.remove(tarefa);
		return "redirect:listaTarefas";
	}

	@RequestMapping("mostraTarefa")
	public String mostra(Long id, Model model) throws SQLException {
		model.addAttribute("tarefa", dao.buscaPorId(id));
		return "tarefa/mostra";
	}

	@RequestMapping("alteraTarefa")
	public String altera(Tarefa tarefa) throws SQLException {
		dao.altera(tarefa);
		return "redirect:listaTarefas";
	}

	
	//@ResponseBody: essa anotação faz a tela mudar sem precisar uma atualização	inteira	da página.
	@RequestMapping("finalizaTarefa")
	public String finaliza(Long id, Model model) throws SQLException {
		dao.finaliza(id);

		model.addAttribute("tarefa", dao.buscaPorId(id));//busca uma tarefa e passa para o JSP através do Model
		return "tarefa/finalizada";
	}

}
