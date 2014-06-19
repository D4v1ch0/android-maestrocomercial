package pe.com.maestro.accounts.adapter;

import java.util.List;

import pe.com.maestro.commercial.R;
import pe.com.maestro.commercial.models.ClientFinancialProduct;
import rp3.util.FileUtils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ClientFinancialProductAdapter extends BaseAdapter {

	private Context context;
	private List<ClientFinancialProduct> data;
	private LayoutInflater inflater = null;
	
	static class ViewHolder {
		ImageView imageView_icon;
		TextView textView_financial_product;
		RadioButton radioButton_select;
	}
	
	public ClientFinancialProductAdapter(Context c, List<ClientFinancialProduct> products){
		this.context = c;
		this.data = products;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {		
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {		
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {		
		return ((ClientFinancialProduct)getItem(arg0)).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.rowlist_financial_product_selector, null);
			
			holder = new ViewHolder();			
			holder.textView_financial_product = (TextView)convertView.findViewById(R.id.textView_financial_product);
			holder.radioButton_select = (RadioButton)convertView.findViewById(R.id.radioButton_select);
			holder.imageView_icon = (ImageView)convertView.findViewById(R.id.imageView_icon);			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ClientFinancialProduct p = (ClientFinancialProduct)getItem(position);
		if(p!=null){
			holder.textView_financial_product.setText(p.getFinancialProduct().getName());
			holder.radioButton_select.setChecked(p.isSelected());
			FileUtils.setBitmapFromInternalStorageAsync(holder.imageView_icon, p.getFinancialProduct().getFileName());			
		}
		
		return convertView;
	}

}
