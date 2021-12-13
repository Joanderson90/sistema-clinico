/*******************************************************************************
Autor: Diego Cerqueira e Joanderson Santos
Componente Curricular: MI Programação
Concluido em: 07/12/2021
Declaro que este código foi elaborado por Diego Cerqueira e Joanderson Santos em dupla e não contém nenhum
trecho de código de outro colega ou de outro autor, tais como provindos de livros e
apostilas, e páginas ou documentos eletrônicos da Internet. Qualquer trecho de código
de outra autoria que não a minha está destacado com uma citação para o autor e a fonte
do código, e estou ciente que estes trechos não serão considerados para fins de avaliação.
******************************************************************************************/

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dao.AgendaConsultaDAO;
import dao.ConsultaDAO;
import date.MyDate;

/**
 * @author Diego Cerqueira e Joanderson Santos
 * @since 2021
 */
public class GerenciadorConsulta {

	/**
	 * Verifica se uma consulta está marcada para algum paciente ou médico
	 * 
	 * @param CPF_Target
	 * @return boolean
	 */
	public static boolean hasConsultaMarcada(String CPF_Target) {

		List<Consulta> consultasCadastradas = ConsultaDAO.getConsultas();

		boolean foundAssociation = consultasCadastradas.stream()
				.anyMatch(consulta -> consulta.getCPF_medico().equals(CPF_Target)
						|| consulta.getCPF_paciente().equals(CPF_Target));

		return foundAssociation;

	}

	/**
	 * Verifica se uma consulta já foi realizada
	 * 
	 * @param CPF_Target
	 * @return boolean
	 */
	public static boolean hasConsultaRealizada(String CPF_Target) {

		List<Consulta> consultasCadastradas = ConsultaDAO.getConsultas();

		boolean foundAssociation = consultasCadastradas.stream()
				.anyMatch(consulta -> (consulta.getCPF_medico().equals(CPF_Target)
						|| consulta.getCPF_paciente().equals(CPF_Target)) && consulta.isRealizada());

		return foundAssociation;

	}

	/**
	 * Metodo que remove todas as consultas de um médico ou paciente
	 * 
	 * @param CPF_Target
	 * @throws Exception
	 */
	public static void removeAllConsultaContainsCPF(String CPF_Target) {

		List<Consulta> consultasQueContainsCPF = getAllConsultaContainsCPF(CPF_Target);

		consultasQueContainsCPF.forEach(consulta -> {

			try {

				ConsultaDAO.deleteConsulta(consulta);

			} catch (Exception e) {

				e.printStackTrace();
			}
		});

	}

	/**
	 * Obtém todas as consultas que contém um certo CPF, este podendo ser do médico
	 * ou paciente.
	 * 
	 * @param CPF_Target cpf alvo.
	 * @return todas as consultas que contém um dado CPF
	 */

	private static List<Consulta> getAllConsultaContainsCPF(String CPF_Target) {

		List<Consulta> consultasCadastradas = ConsultaDAO.getConsultas();

		return consultasCadastradas.stream().filter(consulta -> consulta.getCPF_medico().equals(CPF_Target)
				|| consulta.getCPF_paciente().equals(CPF_Target)).collect(Collectors.toList());

	}

	/**
	 * Metodo que remove todas as agendas de um médico
	 * 
	 * @param CPF_Target
	 * @throws Exception
	 */
	public static void removeAllConsultaAgendaContainsCPF_Medico(String CPF_Target) throws Exception {

		List<AgendaConsulta> agendasContainsCPF = getAllAgendaContainsCPF_Medico(CPF_Target);

		agendasContainsCPF.forEach(agenda -> {

			try {

				AgendaConsultaDAO.deleteAgendaConsulta(agenda);

			} catch (Exception e) {

				e.printStackTrace();
			}
		});

	}

	/**
	 * Obtém todas as agendas que contém um dado CPF de um médico.
	 * 
	 * @param CPF_Target cpf alvo.
	 * @return todas as agendas que contém um dado CPF de um médico.
	 */

	private static List<AgendaConsulta> getAllAgendaContainsCPF_Medico(String CPF_Target) {

		List<AgendaConsulta> agendasCadastradas = AgendaConsultaDAO.getAgendasConsulta();

		return agendasCadastradas.stream().filter(consulta -> consulta.getCPF_medico().equals(CPF_Target))
				.collect(Collectors.toList());

	}

	/**
	 * Verifica se uma consulta ainda não foi realizada
	 * 
	 * @param consultaCadastrada
	 * @return boolean
	 */
	private static boolean isConsultaNotRealizada(Consulta consultaCadastrada) {

		return !consultaCadastrada.isRealizada();
	}

	/**
	 * Verifica se uma consulta possui um cpf associado, seja ele de um médico ou de
	 * um paciente
	 * 
	 * @param consultaTarget
	 * @param CPF_Target
	 * @return boolean
	 */
	private static boolean consultaContainsCPF(Consulta consultaTarget, String CPF_Target) {

		String cpfMedicoConsulta = consultaTarget.getCPF_medico();
		String cpfPacienteConsulta = consultaTarget.getCPF_paciente();

		return cpfMedicoConsulta.equals(CPF_Target) || cpfPacienteConsulta.equals(CPF_Target);
	}

	/**
	 * Retornas as consultas do dia de um medico
	 * 
	 * @param CPF_MEDICO
	 * @return List<Consulta>
	 */
	public static List<Consulta> getConsultasMarcadasHojeByCPF_Medico(String CPF_MEDICO) {

		List<Consulta> allConsultasMarcadas = getAllConsultasMarcadasByCPF_Medico(CPF_MEDICO);

		return allConsultasMarcadas.stream().filter(consulta -> isConsultaMarcadaHoje(consulta))
				.collect(Collectors.toList());
	}

	/**
	 * Retorna todas as consultas marcadas de um médico
	 * 
	 * @param CPF_MEDICO
	 * @return List<Consulta>
	 */
	public static List<Consulta> getAllConsultasMarcadasByCPF_Medico(String CPF_MEDICO) {

		List<Consulta> consultasCadastradas = ConsultaDAO.getConsultas();

		return consultasCadastradas.stream()
				.filter(consulta -> isConsultaNotRealizada(consulta) && consultaContainsCPF(consulta, CPF_MEDICO))
				.collect(Collectors.toList());

	}

	/**
	 * Verifica se uma consulta foi marcada no dia
	 * 
	 * @param consultaCadastrada
	 * @return boolean
	 */
	private static boolean isConsultaMarcadaHoje(Consulta consultaCadastrada) {

		String dataAtual = new MyDate().getCurrentDate();
		String dataMarcadaConsulta = consultaCadastrada.getData();

		return dataAtual.equals(dataMarcadaConsulta);
	}

	/**
	 * Retorna o histórico de consultas de um paciente
	 * 
	 * @param CPF_Paciente
	 * @return List<HistoricoConsulta>
	 */
	public static List<HistoricoConsulta> getHistoryConsultasPaciente(String CPF_Paciente) {

		List<Consulta> consultasRealizadasPaciente = getAllConsultasRealizadasByCPF_Paciente(CPF_Paciente);

		List<HistoricoConsulta> historyConsultasPaciente = new ArrayList<>();

		consultasRealizadasPaciente.forEach(consulta -> historyConsultasPaciente.add(new HistoricoConsulta(consulta)));

		return historyConsultasPaciente;

	}

	private static List<Consulta> getAllConsultasRealizadasByCPF_Paciente(String CPF_Paciente) {

		List<Consulta> consultasCadastradas = ConsultaDAO.getConsultas();

		return consultasCadastradas.stream()
				.filter(consulta -> isConsultaRealizada(consulta) && consultaContainsCPF(consulta, CPF_Paciente))
				.collect(Collectors.toList());

	}

	/**
	 * Verifica se uma consulta já foi realizada
	 * 
	 * @param consultaCadastrada
	 * @return boolean
	 */
	private static boolean isConsultaRealizada(Consulta consultaCadastrada) {

		return consultaCadastrada.isRealizada();
	}

	/**
	 * Obtém todas Agendas Consultas Não marcadas.
	 * 
	 * @return agendas consultas não marcadas.
	 */

	public static List<AgendaConsulta> getAgendasConsultaNaoMarcadas() {

		List<AgendaConsulta> agendasConsultaCadastradas = AgendaConsultaDAO.getAgendasConsulta();

		return agendasConsultaCadastradas.stream().filter(agenda -> isAgendaNaoMaracda(agenda))
				.collect(Collectors.toList());

	}

	/**
	 * Verifica se uma Agenda Consulta não foi marcada.
	 * 
	 * @param agenda a ser verificada.
	 * @return true se a agenda não foi marcada, false caso contrário.
	 */

	private static boolean isAgendaNaoMaracda(AgendaConsulta agenda) {

		return !agenda.isMarcada();
	}

}
