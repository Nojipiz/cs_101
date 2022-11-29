package persistence

// DESCOMENTREAR----------------------------------------------------------------------------!
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
object FileManagerJson {
    // MODIFICAR TAGS---------------------------------------------------------------------------|
    private const val TAG_LIST = "list" //dejar taglist
    private const val TAG_ID = "id"
    private const val TAG_NAME = "name"
    private const val TAG_PRICE = "price"
    private const val TAG_IMG = "img"
    private const val TAG_PROVIDER = "provider"
    private const val DB_PATH = "./db/list.json" //dejar la ruta del archivo
    //	@SuppressWarnings("unchecked")
    //	public static void write(ArrayList<ClaseLeturaEscritura> list) {
    //		JSONObject jsonfile = new JSONObject();
    //		JSONArray jsonList = new JSONArray();
    //		for (ClaseLeturaEscritura componentList : list) {
    //			JSONObject jsonProfile = new JSONObject();
    //			jsonProfile.put(TAG_ID, componentList.getId());
    //			jsonProfile.put(TAG_NAME, componentList.getName());
    //			jsonProfile.put(TAG_PRICE, componentList.getPrice());
    //			jsonProfile.put(TAG_PROVIDER, componentList.getProvider());
    //			jsonProfile.put(TAG_IMG, componentList.getImgPath());
    //			jsonList.add(jsonProfile);
    //		}
    //		jsonfile.put(TAG_LIST, jsonList);
    //		try (FileWriter file = new FileWriter(DB_PATH, false)) {
    //			file.write(jsonfile.toJSONString());
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
    //	}
    //	@SuppressWarnings("unchecked")
    //	public static ArrayList<ClaseLeturaEscritura> read() {
    //		ArrayList<ClaseLeturaEscritura> list = new ArrayList<ClaseLeturaEscritura>();
    //		JSONParser parser = new JSONParser();
    //		try (Reader reader = new FileReader(DB_PATH)) {
    //			JSONObject jsonObject = (JSONObject) parser.parse(reader);
    //			JSONArray jsonList = (JSONArray) jsonObject.get(TAG_LIST);
    //			Iterator<JSONObject> iterator = jsonList.iterator();
    //			while (iterator.hasNext()) {
    //				JSONObject jsonListComponent = (JSONObject) iterator.next();
    //				list.add(new ClaseLeturaEscritura((String)jsonListComponent.get(TAG_NAME),						
    //						(Long)jsonListComponent.get(TAG_PRICE),
    //						(String)jsonListComponent.get(TAG_PROVIDER),
    //						(String)jsonListComponent.get(TAG_IMG)));
    //			}
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		} catch (ParseException e) {
    //			e.printStackTrace();
    //		}
    //		return list;
    //	}
    // ACTIVAR METODOS DE LECTURA Y ESCRITURA EN EL MODELO-----------------------------------------|
    // COMPROBAR LA LECTURA DE LA LISTA (sin sobre escritura)---------------------------------|
    //	public static void main(String[] args) {
    //		for (ClaseLeturaEscritura componentList : read()) {
    //			System.out.println(componentList.getId());			
    //		}
    //	}
}