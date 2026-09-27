/**
 * Representa um produto que não possui data de validade.
 * O valor de venda segue exatamente a regra original: preço de custo
 * acrescido da margem de lucro, sem nenhum desconto adicional.
 */
public class ProdutoNaoPerecivel extends Produto {

	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
		super(desc, precoCusto, margemLucro);
	}

	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public ProdutoNaoPerecivel(String desc, double precoCusto) {
		super(desc, precoCusto);
	}

	/**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	@Override
	public double valorVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}
}
