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
 * A anota��o @Controller indica ao Spring que os m�todos dessa classe s�o a��es
 * (Action).
 * 
 * Podemos criar um m�todo de qualquer nome dentro dessa classe, desde que ele
 * esteja com a anota��o @RequestMapping . A anota��o @RequestMapping recebe um
 * atributo chamado value que indica qual ser� a URL utilizada para invocar o
 * m�todo, como esse atributo j� � o padr�o n�o precisamos definir. Portanto, se
 * colocarmos o valor olaMundoSpring acessaremos o m�todo dentro do
 * nosso @Controller pela URL http://localhost:8080/fj21-tarefas/olaMundoSpring
 * 
 * Dentro dessa nossa classe TarefasController em nenhum momento temos um
 * HttpServletRequest para pegarmos os par�metros enviados na requisi��o e
 * montarmos o objeto tarefa . Uma das grandes vantagens de frameworks modernos
 * � que eles conseguem popular os objetos para n�s. Basta que de alguma forma,
 * n�s fa�amos uma liga��o entre o campo que est� na tela com o objeto que
 * queremos popular. E com o Spring MVC n�o � diferente. Essa liga��o � feita
 * atrav�s da cria��o de um par�metro do m�todo adiciona . Esse par�metro � o
 * objeto que dever� ser populado pelo Spring MVC com os dados que vieram da
 * requisi��o.
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
	 * � preciso carregar o JSP no navegador, mas o acesso direto a pasta WEB-INF �
	 * proibido pelo servlet-container e consequentemente n�o � poss�vel acessar o
	 * formul�rio. Para resolver isso vamos criar uma nova a��o que tem a finalidade
	 * de chamar o formul�rio apenas
	 */
	@RequestMapping("novaTarefa")
	public String form() {
		return "tarefa/formulario";
	}

	/**
	 * Os dados para a exibi��o na tela e o nome da p�gina JSP s�o encapsulados pelo
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

	
	//@ResponseBody: essa anota��o faz a tela mudar sem precisar uma atualiza��o	inteira	da p�gina.
	@RequestMapping("finalizaTarefa")
	public String finaliza(Long id, Model model) throws SQLException {
		dao.finaliza(id);

		model.addAttribute("tarefa", dao.buscaPorId(id));//busca uma tarefa e passa para o JSP atrav�s do Model
		return "tarefa/finalizada";
	}

}
