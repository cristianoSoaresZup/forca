package br.com.zup.estrelas.primeiraForca;

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

	public static String removerAcentos(String str) {
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String selecionaPalavra() throws IOException {
		Random gerador = new Random();
		FileReader primeiroLeitor = new FileReader("C:\\Users\\Zupper\\eclipse-workspace\\primeiro-projeto\\palavras.txt");
		FileReader segundoLeitor = new FileReader("C:\\Users\\Zupper\\eclipse-workspace\\primeiro-projeto\\palavras.txt");
		BufferedReader contarPalavras = new BufferedReader(primeiroLeitor);
		BufferedReader armazenarPalavras = new BufferedReader(segundoLeitor);
		
		String palavraLida;
		int tamanhoDoArray = 0, numeroDeLinhasDoArquivo = 0;
		
		palavraLida = contarPalavras.readLine();
		while (palavraLida != null) {
			tamanhoDoArray++;
		}

		String[] palavrasArmazenadas = new String[tamanhoDoArray];
		while ((palavraLida = armazenarPalavras.readLine()) != null) {
			palavrasArmazenadas[numeroDeLinhasDoArquivo] = palavraLida;
			numeroDeLinhasDoArquivo++;
		}

		String palavraSelecionada = palavrasArmazenadas[gerador.nextInt(tamanhoDoArray)];

		removerAcentos(palavraSelecionada);

		armazenarPalavras.close();
		contarPalavras.close();
		segundoLeitor.close();
		primeiroLeitor.close();

		return palavraSelecionada;

	}

	public static boolean buscarNoArray(char x, char y[]) {
		boolean existe = false;
		for (int i = 0; i < y.length; i++) {
			if (x == y[i]) {
				existe = true;
			}
		}
		return existe;
	}

	public static void main(String[] args) throws IOException {

		Scanner teclado = new Scanner(System.in);
		FileWriter adicionaPalavra = new FileWriter("palavras.txt", true);

		String palavraDoJogo = selecionaPalavra().toUpperCase();

		int erros = 0;
		int acertos = 0;
		int numeroTentativa = 0;
		char[] palavraOculta = new char[palavraDoJogo.length()];
		char[] traco = new char[palavraDoJogo.length()];
		char[] tentativasArmazenadas = new char[36];

		for (int i = 0; i < palavraDoJogo.length(); i++) {
			traco[i] = '_';
			palavraOculta[i] = palavraDoJogo.charAt(i);
		}

		System.out.println("<<<<Jogo da Forca>>>>");

		while (erros < 6 && buscarNoArray('_', traco)) {

			System.out.println("\n\nA palavra é: " + Arrays.toString(traco).replaceAll(",", " "));
			System.out.println("\nDigite uma letra (números e caracteres" + "\n especiais serão contados como erro, porém,"
					+ "\no hífem é aceito para palavras compostas. Se digitá-lo, e ele estiver na palavra computado como acerto.");

			String letraDigitada = teclado.next().toUpperCase();
			char letra = removerAcentos(letraDigitada).charAt(0);

			if (!buscarNoArray(letra, tentativasArmazenadas)) {
				tentativasArmazenadas[numeroTentativa] = letra;
				numeroTentativa++;

				for (int i = 0; i < palavraDoJogo.length(); i++) {
					if (letra == palavraOculta[i]) {
						traco[i] = letra;

					}

				}

				if (buscarNoArray(letra, traco)) {
					acertos++;
				} else {
					erros++;
				}

				System.out
						.println("---letras tentadas: " + Arrays.toString(tentativasArmazenadas).replaceAll(",", " "));
				System.out.println("---acertos: " + acertos + "\n---erros: " + erros);
				System.out.println("---tentativas: " + numeroTentativa);
			} else {
				System.out.println("Voçê já tentou essa letra. \nTente mais uma vez com uma letra ainda não digitada.");
			}

		}

		if (!buscarNoArray('_', traco))

		{
			System.out.println(">>>>> Parabéns! Voçê acertou a palavra! <<<<< \n" + "A palavra é: " + palavraDoJogo);
			System.out.println("Como vc venceu, ganhou o direito de inserir uma nova palavra no banco de dados!");
			System.out.println("Digite a palavra, ou digite 0 para terminar:");

			String novaPalavra = teclado.next().toUpperCase();
			while (novaPalavra != "") {
				adicionaPalavra.append("\n" + novaPalavra);
				System.out.println("Até a próxima!");
				break;
			}
			

		} else {
			System.out.println("     =====================     \n     !!!!!!!!ERROU!!!!!!!!     " + "\n"
					+ "Você não conseguiu acertar a palavra antes de ser enforcado\n " + "A palavra correta é: "
					+ palavraDoJogo);
		}

		adicionaPalavra.close();
		teclado.close();
	}
}