import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ABB<K, V> implements IMapeamento<K, V>{

	// atributos
	private No<K, V> raiz;
	private Comparator<K> comparador;
	private int quantidade;
	private V itemRemovido;
	private long comparacoes;
	private long inicio;
	private long termino;
	
	/**
	 * Construtor da classe.
	 * @param comparador o comparador a ser utilizado para organizar os elementos da árvore.  
	 */
	public ABB(Comparator<K> comparador) {
		this.raiz = null;
		this.comparador = comparador;
		this.quantidade = 0;
	}
	
	/**
	 * Construtor da classe, usando a ordem natural das chaves (as chaves devem implementar Comparable).
	 */
	public ABB() {
		this(ABB.<K>ordemNatural());
	}
	
	/**
	 * Construtor que cria uma nova árvore, organizada por uma nova chave, com os mesmos itens de outra árvore.
	 * Os nós são copiados em pré-ordem, para que a nova árvore mantenha um formato semelhante ao da original.
	 * @param original árvore com os itens a serem copiados.
	 * @param extratorChave função que extrai, de cada item, a chave da nova árvore.
	 */
	public <K2> ABB(ABB<K2, V> original, Function<V, K> extratorChave) {
		this();
		copiar(original.raiz, extratorChave);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Comparator<T> ordemNatural() {
		return (a, b) -> ((Comparable<T>) a).compareTo(b);
	}
	
	private <K2> void copiar(No<K2, V> no, Function<V, K> extratorChave) {
		if (no != null) {
			inserir(extratorChave.apply(no.getItem()), no.getItem());
			copiar(no.getEsquerda(), extratorChave);
			copiar(no.getDireita(), extratorChave);
		}
	}
	
	@Override
	public long getComparacoes() {
		return comparacoes;
	}

	@Override
	public double getTempo() {
		return (termino - inicio) / 1_000_000;
	}
	
	/** Informa se a árvore está vazia. */
	public boolean vazia() {
		return (raiz == null);
	}
	
	@Override
	public int tamanho() {
		return quantidade;
	}
	
	// ---------------------------------------------------------------- inserção
	
	/**
	 * Insere um item na árvore.
	 * @return a quantidade de itens da árvore após a inserção.
	 * @throws IllegalArgumentException se já existir um item com a mesma chave.
	 */
	@Override
	public int inserir(K chave, V item) {
		comparacoes = 0;
		inicio = System.nanoTime();
		try {
			raiz = inserir(raiz, chave, item);
			quantidade++;
		} finally {
			termino = System.nanoTime();
		}
		return quantidade;
	}
	
	private No<K, V> inserir(No<K, V> no, K chave, V item) {
		if (no == null)
			return new No<K, V>(chave, item);
		
		comparacoes++;
		int comparacao = comparador.compare(chave, no.getChave());
		
		if (comparacao == 0)
			throw new IllegalArgumentException("O item já foi inserido anteriormente na árvore.");
		else if (comparacao < 0)
			no.setEsquerda(inserir(no.getEsquerda(), chave, item));
		else
			no.setDireita(inserir(no.getDireita(), chave, item));
		
		return no;
	}
	
	// ---------------------------------------------------------------- pesquisa
	
	/**
	 * Pesquisa um item pela chave.
	 * @throws NoSuchElementException se a chave não estiver na árvore.
	 */
	@Override
	public V pesquisar(K chave) {
		comparacoes = 0;
		inicio = System.nanoTime();
		try {
			return pesquisar(raiz, chave);
		} finally {
			termino = System.nanoTime();
		}
	}
	
	private V pesquisar(No<K, V> no, K chave) {
		if (no == null)
			throw new NoSuchElementException("O item não foi localizado na árvore!");
		
		comparacoes++;
		int comparacao = comparador.compare(chave, no.getChave());
		
		if (comparacao == 0)
			return no.getItem();
		else if (comparacao < 0)
			return pesquisar(no.getEsquerda(), chave);
		else
			return pesquisar(no.getDireita(), chave);
	}
	
	// ---------------------------------------------------------------- remoção
	
	/**
	 * Remove o item com a chave informada e o retorna.
	 * @throws NoSuchElementException se a chave não estiver na árvore.
	 */
	@Override
	public V remover(K chave) {
		comparacoes = 0;
		inicio = System.nanoTime();
		try {
			itemRemovido = null;
			raiz = remover(raiz, chave);
			return itemRemovido;
		} finally {
			termino = System.nanoTime();
		}
	}
	
	private No<K, V> remover(No<K, V> no, K chave) {
		if (no == null)
			throw new NoSuchElementException("O item a ser removido não foi localizado na árvore!");
		
		comparacoes++;
		int comparacao = comparador.compare(chave, no.getChave());
		
		if (comparacao < 0)
			no.setEsquerda(remover(no.getEsquerda(), chave));
		else if (comparacao > 0)
			no.setDireita(remover(no.getDireita(), chave));
		else {
			itemRemovido = no.getItem();
			quantidade--;
			no = removerNoAtual(no);
		}
		return no;
	}
	
	/** Remove o nó recebido, retornando o nó que deve ocupar o seu lugar. */
	private No<K, V> removerNoAtual(No<K, V> no) {
		if (no.getEsquerda() == null)
			return no.getDireita();
		if (no.getDireita() == null)
			return no.getEsquerda();
		
		// dois filhos: o nó assume os dados do sucessor (menor chave da subárvore direita)
		No<K, V> sucessor = no.getDireita();
		while (sucessor.getEsquerda() != null)
			sucessor = sucessor.getEsquerda();
		
		no.setChave(sucessor.getChave());
		no.setItem(sucessor.getItem());
		no.setDireita(removerMenor(no.getDireita()));
		return no;
	}
	
	/** Remove o nó de menor chave da subárvore recebida. */
	private No<K, V> removerMenor(No<K, V> no) {
		if (no.getEsquerda() == null)
			return no.getDireita();
		no.setEsquerda(removerMenor(no.getEsquerda()));
		return no;
	}
	
	// ---------------------------------------------------------------- recorte
	
	/**
	 * Retorna uma lista, em ordem crescente de chave, com os itens cujas chaves estão entre
	 * chaveInicio e chaveFinal, inclusive. Subárvores fora do intervalo não são visitadas.
	 * Se chaveInicio for maior que chaveFinal, a lista retornada é vazia.
	 */
	public Lista<V> recortar(K chaveInicio, K chaveFinal) {
		Lista<V> recorte = new Lista<V>();
		comparacoes = 0;
		inicio = System.nanoTime();
		try {
			recortar(raiz, chaveInicio, chaveFinal, recorte);
		} finally {
			termino = System.nanoTime();
		}
		return recorte;
	}
	
	private void recortar(No<K, V> no, K chaveInicio, K chaveFinal, Lista<V> recorte) {
		if (no == null)
			return;
		
		comparacoes += 2;
		int emRelacaoAoInicio = comparador.compare(chaveInicio, no.getChave());
		int emRelacaoAoFinal = comparador.compare(chaveFinal, no.getChave());
		
		if (emRelacaoAoInicio < 0)   // há chaves do intervalo à esquerda
			recortar(no.getEsquerda(), chaveInicio, chaveFinal, recorte);
		
		if (emRelacaoAoInicio <= 0 && emRelacaoAoFinal >= 0)
			recorte.inserir(no.getItem());
		
		if (emRelacaoAoFinal > 0)    // há chaves do intervalo à direita
			recortar(no.getDireita(), chaveInicio, chaveFinal, recorte);
	}
	
	// ---------------------------------------------------------------- percurso
	
	/** Percorre a árvore em ordem, retornando um item por linha. */
	@Override
	public String percorrer() {
		return caminhamentoEmOrdem();
	}
	
	public String caminhamentoEmOrdem() {
		if (vazia())
			return "A árvore está vazia!";
		StringBuilder texto = new StringBuilder();
		emOrdem(raiz, texto);
		return texto.toString();
	}
	
	private void emOrdem(No<K, V> no, StringBuilder texto) {
		if (no != null) {
			emOrdem(no.getEsquerda(), texto);
			texto.append(no.getItem()).append("\n");
			emOrdem(no.getDireita(), texto);
		}
	}
	
	@Override
	public String toString() {
		return caminhamentoEmOrdem();
	}
}
