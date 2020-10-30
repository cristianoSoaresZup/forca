
package br.com.zup.estrelas.forcaRefatorada.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ForcaCSS {
	
	//FIXME: usei funções e constantes para dar uma limpada no main
	
	public static final String ENUNCIADO_DO_JOGO = "\t====================<<<<JOGO DA FORCA>>>>====================\n"
										+ "\t||== Tente adivinhar a palavra escondida apenas contando ==||\n"
										+ "\t||==============  os traços apresentados.  ================||\n"
										+ "\t||=========================================================||\n"
										+ "\t||===  A cada acerto, as letras irão sendo reveladas,   ===||\n"
										+ "\t||===  até que se forme a palavra oculta pelos traços.  ===||\n"
										+ "\t=============================================================\n";

	public static final String AVISO_DE_INSIRA_UMA_LETRA = "\t|                        Digite uma letra                          |\n"
			                                             + "\t|     Números e caracteres especiais serão contados como erro      |\n"
			                                             + "\t|         O hífem é aceito para palavras compostas                 |\n";

	public static final String VOCE_GANHOU = "\t|>>>>>   Parabéns! Você acertou a palavra!   <<<<<|\n"
			                               + "\t|>> Como prêmio, você poderá inserir uma nova   <<|\n"
			                               + "\t|>>    palavra no banco de palavras do jogo     <<|\n\n"
			                               + "\t|>>   Digite a palavra ou apenas digite 0 para  <<|\n"
			                               + "\t|>>>>             encerrar                    <<<<|\n";

	public static final String VOCE_PERDEU = "\t=======================================================\n"
			                               + "\t||||||||||||||||||      ERROU!!!   ||||||||||||||||||||\n"
			                               + "\t=======================================================\n"
			                               + "\t=======================================================\n"
			                               + "\t====  Você não conseguiu adivinhar a palavra e foi ====\n"
			                               + "\t###############    !!!ENFORCADO!!!    #################\n"
			                               + "\t=======================================================\n";
	
	public static String removerAcentos(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String selecionaPalavra() throws IOException {
		Random gerador = new Random();
		FileReader leitorDoArquivo = new FileReader("palavras.txt");
		
		//Conforme dica, pesquisei e usei outra abordagem na questão do uso de dois leitores de arquivo
		
		BufferedReader lerEContarPalavras = new BufferedReader(leitorDoArquivo);

		String palavraLida;
		int tamanhoDoArray = 0, numeroDeLinhasDoArquivo = 0;
		
		lerEContarPalavras.mark(500000);
		while ((palavraLida = lerEContarPalavras.readLine()) != null) {
			tamanhoDoArray++;
		}
		
		String[] palavrasArmazenadas = new String[tamanhoDoArray];
		lerEContarPalavras.reset();
		while ((palavraLida = lerEContarPalavras.readLine()) != null) {
			palavrasArmazenadas[numeroDeLinhasDoArquivo] = palavraLida;
			numeroDeLinhasDoArquivo++;
		}

		String palavraSelecionada = palavrasArmazenadas[gerador.nextInt(tamanhoDoArray)];
		
		lerEContarPalavras.close();
		leitorDoArquivo.close();
		
		String palavraDaForca = removerAcentos(palavraSelecionada).toUpperCase();
		
		//conforme orientação, criei uma variável para receber o retorno da 
		//função removerAcentos.
		
		return palavraDaForca;

	}

	public static boolean buscarNoArray(char caractereDaPesquisa, char vetorASerPesquisado[]) {
		//as variáveis que representam os parâmetros de entrada
		//foram renomeadas
		boolean existe = false;
		for (int i = 0; i < vetorASerPesquisado.length; i++) {
			if (caractereDaPesquisa == vetorASerPesquisado[i]) {
				existe = true;
			}
		}
		return existe;
	}

	public static void preencherArrayOculto(String palavraDoJogo, char[] palavraASerRevelada, char [] palavraOculta ) {
		for (int i = 0; i < palavraDoJogo.length(); i++) {
			palavraASerRevelada[i] = '_';
			palavraOculta[i] = palavraDoJogo.charAt(i);
		}
	}
	
	public static void iterarArrayDaPalavraDoJogo(char[]palavraOculta, char[]palavraASerRevelada, char letra) {
		for (int i = 0; i < palavraOculta.length; i++) {
			if (letra == palavraOculta[i]) {
				palavraASerRevelada[i] = letra;
			}
		}
	}
	
	public static void main(String[] args) throws IOException {

		Scanner teclado = new Scanner(System.in);
		//FileWriter foi mudado para momento mais oportuno, conforme dica

		String palavraDoJogo = selecionaPalavra();

		int erros = 0;
		int acertos = 0;
		int numeroTentativa = 0;
		char[] palavraOculta = new char[palavraDoJogo.length()];
		//renomeei o array traco para palavraASerRevelada. Faz mais sentido, já que os traços vão 
		//sendo substituidos pelos caracteres da palavra do jogo conforme os acertos
		char[] palavraASerRevelada = new char[palavraDoJogo.length()];
		char[] tentativasArmazenadas = new char[36];

		preencherArrayOculto(palavraDoJogo, palavraASerRevelada, palavraOculta);
		
		System.out.println(ENUNCIADO_DO_JOGO);

		while (erros < 6 && buscarNoArray('_', palavraASerRevelada)) {

			System.out.println("\n\t ====>>>>    A palavra é: " 
			+ Arrays.toString(palavraASerRevelada).replaceAll(",", " ") + "\n\n");
			
			System.out.println(AVISO_DE_INSIRA_UMA_LETRA);

			String letraDigitada = teclado.next().toUpperCase();
			char letra = removerAcentos(letraDigitada).charAt(0);

			if (!buscarNoArray(letra, tentativasArmazenadas)) {
				tentativasArmazenadas[numeroTentativa] = letra;
				numeroTentativa++;

				iterarArrayDaPalavraDoJogo(palavraOculta, palavraASerRevelada, letra);

				if (buscarNoArray(letra, palavraASerRevelada)) {
					acertos++;
				} else {
					erros++;
				}

				System.out.println("\t---letras tentadas: " + Arrays.toString(tentativasArmazenadas).replaceAll(",", " "));
				System.out.println("\t---acertos: " + acertos + "\n\t---erros: " + erros);
				System.out.println("\t---tentativas: " + numeroTentativa);
			} else {
				System.out.println("\tVocê já tentou essa letra. \n\tTente mais uma vez com uma letra ainda não digitada.");
			}

		}

		if (!buscarNoArray('_', palavraASerRevelada)) {
			
			System.out.println(VOCE_GANHOU);
			String palavra = teclado.next();
			String novaPalavra = removerAcentos(palavra);
			
			//FIXME: FileWriter mais próximo de sua utilização. Também utilizei a função removerAcentos para filtrar 
			//a palavra que será adiciconada no arquivo
			//Também corrigi a lógica. O código estava salvando "0" no fim do arquivo, em vez de terminar o programa
			if (!novaPalavra.equals("0")) {
				FileWriter adicionaPalavra = new FileWriter("palavras.txt", true);
				adicionaPalavra.append("\n" + novaPalavra);
				adicionaPalavra.close();
			}		
			
		} else {
			System.out.println(VOCE_PERDEU);
			System.out.println("\t====>>> A palavra correta é: "+ palavraDoJogo + " <<<====\n");
		}
		System.out.println("Até a próxima!");
		teclado.close();
	}
}
