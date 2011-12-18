package fit.main;

//classe auxiliar para apresentaçao de listview personalizada no plano de treino
public class ListMenuItem {
	public String item;
	public String subitem;
	public String subitem2;
	public String bool_pago;

	public ListMenuItem(String item, String subitem, String subitem2) {
		this.item = item;
		this.subitem = subitem;
		this.subitem2 = subitem2;
	}
	
	//construtor para imagem na listview dos pagamentos
	public ListMenuItem(String item, String subitem, String subitem2, String bool_pago) {
		this.item = item;
		this.subitem = subitem;
		this.subitem2 = subitem2;
		this.bool_pago = bool_pago;
	}
}