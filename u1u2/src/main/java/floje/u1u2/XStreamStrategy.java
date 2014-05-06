package floje.u1u2;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;

public class XStreamStrategy implements fpt.com.SerializableStrategy,AutoCloseable{
    
	private XStream xstream;
	private FileWriter fw=null;
	private FileReader fr=null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	
	public XStreamStrategy()throws Exception{
		xstream = new XStream(new DomDriver());
		xstream.aliasField("anzahl", floje.u1u2.Product.class, "quantity");
		xstream.aliasField("preis", floje.u1u2.Product.class, "price");
		
		xstream.useAttributeFor(floje.u1u2.Product.class, "id");
		xstream.registerLocalConverter(floje.u1u2.Product.class, "id", new IDConverter());	
		xstream.registerLocalConverter(floje.u1u2.Product.class, "price", new PriceConverter());

		xstream.alias("ware", floje.u1u2.Product.class);
	}
	
	@Override
	public Product readObject() throws IOException{
		if(fr==null && fw == null){
			fr = new FileReader("products.xstream.xml");
		}
		if (fr == null) {
			throw new IOException("This strategy is allready writing to products.xstream.xml!");
		}
		if(in==null){
			in = xstream.createObjectInputStream(fr);
		}
		Product pr=null;
		try {
			pr = (Product)(in.readObject());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return pr;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if(fw == null && fr == null){
			fw = new FileWriter("products.xstream.xml");
		}
		if (fw == null) {
			throw new IOException("This strategy is allready reading from products.xml!");
		}
		if(out==null){
			out = xstream.createObjectOutputStream(fw,"waren");
		}
		out.writeObject(obj);
		out.flush();
	}

	@Override
	public void close() throws IOException {
		if(out!=null){
			out.close();
			//out=null;
		}
		if(in!=null){
			in.close();
			//in=null;
		}
		if(fw!=null){
			fw.close();
			//fw=null;
		}
		if(fr!=null){
			fr.close();
			//fr=null;
		}
		
		
	}
	

	

}
