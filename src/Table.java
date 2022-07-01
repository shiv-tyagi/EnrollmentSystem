
public class Table {
	String name;
	String attributes;
	String constraints;
	String foreignKeys;
	
	public Table(String rawDef) {
		name = attributes = constraints = foreignKeys = null;
		this.parseRawDef(rawDef);
	}
	
	public void parseRawDef(String rawDef) {
		String tokens[] = rawDef.split("-");
		this.name = tokens[0].trim();
		this.attributes = tokens[1].trim();
		this.constraints = tokens[2].trim();
		this.foreignKeys = tokens[3].trim();
	}

}
