package ds;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.*;
import org.bson.Document;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Name: Saloni Priyani
// Andrew ID: spriyani
public class TranslateModel {

    // This function takes in a string 'textToDetect' and uses an API to detect the language of the input text.
    // It then returns the detected language in JSON format.
    // If there is an error in accessing the API or the data returned by the API is invalid, the function returns an empty string.
    public String detectLang(String textToDetect) throws IOException, JsonSyntaxException, JsonIOException {
        // API key for Detect Language API
        String apiKey = "a864931455caad43d54c18f988ce0028";
        // Input text to detect language for
        String inputText = textToDetect;
        // URL to access Detect Language API with the input text to detect language for
        String urlString = "https://ws.detectlanguage.com/0.2/detect?q=" + URLEncoder.encode(inputText, "UTF-8");
        // Print the input text to console
        System.out.println("Input String: " + inputText);

        // Create URL object for Detect Language API
        URL url = new URL(urlString);
        // Open connection to API
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Set request method to GET
        connection.setRequestMethod("GET");
        // Set authorization header with API key
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);

        // Read the response from the API
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        // Close the reader and disconnect the connection
        reader.close();
        connection.disconnect();

        // Print the response from the API to console
        System.out.println("Response from Detect Language API: " + response.toString());

        // Parse the JSON response from the API
        JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
        String data = String.valueOf(jsonObject.get("data"));
        JsonObject jsonObject2 = new JsonParser().parse(data).getAsJsonObject();
        JsonArray js = (JsonArray) jsonObject2.get("detections");
        JsonObject firstPrediction = new JsonParser().parse(String.valueOf(js.get(0))).getAsJsonObject();
        String lang = String.valueOf(firstPrediction.get("language"));
        // Remove the quotes from the language code
        lang = lang.substring(1, lang.length()-1);
        System.out.println(lang);

        // Create a list of languages with their codes and names
        String languagesList = "[{\"code\":\"aa\",\"name\":\"AFAR\"},{\"code\":\"ab\",\"name\":\"ABKHAZIAN\"},{\"code\":\"af\",\"name\":\"AFRIKAANS\"},{\"code\":\"ak\",\"name\":\"AKAN\"},{\"code\":\"am\",\"name\":\"AMHARIC\"},{\"code\":\"ar\",\"name\":\"ARABIC\"},{\"code\":\"as\",\"name\":\"ASSAMESE\"},{\"code\":\"ay\",\"name\":\"AYMARA\"},{\"code\":\"az\",\"name\":\"AZERBAIJANI\"},{\"code\":\"ba\",\"name\":\"BASHKIR\"},{\"code\":\"be\",\"name\":\"BELARUSIAN\"},{\"code\":\"bg\",\"name\":\"BULGARIAN\"},{\"code\":\"bh\",\"name\":\"BIHARI\"},{\"code\":\"bi\",\"name\":\"BISLAMA\"},{\"code\":\"bn\",\"name\":\"BENGALI\"},{\"code\":\"bo\",\"name\":\"TIBETAN\"},{\"code\":\"br\",\"name\":\"BRETON\"},{\"code\":\"bs\",\"name\":\"BOSNIAN\"},{\"code\":\"bug\",\"name\":\"BUGINESE\"},{\"code\":\"ca\",\"name\":\"CATALAN\"},{\"code\":\"ceb\",\"name\":\"CEBUANO\"},{\"code\":\"chr\",\"name\":\"CHEROKEE\"},{\"code\":\"co\",\"name\":\"CORSICAN\"},{\"code\":\"crs\",\"name\":\"SESELWA\"},{\"code\":\"cs\",\"name\":\"CZECH\"},{\"code\":\"cy\",\"name\":\"WELSH\"},{\"code\":\"da\",\"name\":\"DANISH\"},{\"code\":\"de\",\"name\":\"GERMAN\"},{\"code\":\"dv\",\"name\":\"DHIVEHI\"},{\"code\":\"dz\",\"name\":\"DZONGKHA\"},{\"code\":\"egy\",\"name\":\"EGYPTIAN\"},{\"code\":\"el\",\"name\":\"GREEK\"},{\"code\":\"en\",\"name\":\"ENGLISH\"},{\"code\":\"eo\",\"name\":\"ESPERANTO\"},{\"code\":\"es\",\"name\":\"SPANISH\"},{\"code\":\"et\",\"name\":\"ESTONIAN\"},{\"code\":\"eu\",\"name\":\"BASQUE\"},{\"code\":\"fa\",\"name\":\"PERSIAN\"},{\"code\":\"fi\",\"name\":\"FINNISH\"},{\"code\":\"fj\",\"name\":\"FIJIAN\"},{\"code\":\"fo\",\"name\":\"FAROESE\"},{\"code\":\"fr\",\"name\":\"FRENCH\"},{\"code\":\"fy\",\"name\":\"FRISIAN\"},{\"code\":\"ga\",\"name\":\"IRISH\"},{\"code\":\"gd\",\"name\":\"SCOTS_GAELIC\"},{\"code\":\"gl\",\"name\":\"GALICIAN\"},{\"code\":\"gn\",\"name\":\"GUARANI\"},{\"code\":\"got\",\"name\":\"GOTHIC\"},{\"code\":\"gu\",\"name\":\"GUJARATI\"},{\"code\":\"gv\",\"name\":\"MANX\"},{\"code\":\"ha\",\"name\":\"HAUSA\"},{\"code\":\"haw\",\"name\":\"HAWAIIAN\"},{\"code\":\"hi\",\"name\":\"HINDI\"},{\"code\":\"hmn\",\"name\":\"HMONG\"},{\"code\":\"hr\",\"name\":\"CROATIAN\"},{\"code\":\"ht\",\"name\":\"HAITIAN_CREOLE\"},{\"code\":\"hu\",\"name\":\"HUNGARIAN\"},{\"code\":\"hy\",\"name\":\"ARMENIAN\"},{\"code\":\"ia\",\"name\":\"INTERLINGUA\"},{\"code\":\"id\",\"name\":\"INDONESIAN\"},{\"code\":\"ie\",\"name\":\"INTERLINGUE\"},{\"code\":\"ig\",\"name\":\"IGBO\"},{\"code\":\"ik\",\"name\":\"INUPIAK\"},{\"code\":\"is\",\"name\":\"ICELANDIC\"},{\"code\":\"it\",\"name\":\"ITALIAN\"},{\"code\":\"iu\",\"name\":\"INUKTITUT\"},{\"code\":\"iw\",\"name\":\"HEBREW\"},{\"code\":\"ja\",\"name\":\"JAPANESE\"},{\"code\":\"jw\",\"name\":\"JAVANESE\"},{\"code\":\"ka\",\"name\":\"GEORGIAN\"},{\"code\":\"kha\",\"name\":\"KHASI\"},{\"code\":\"kk\",\"name\":\"KAZAKH\"},{\"code\":\"kl\",\"name\":\"GREENLANDIC\"},{\"code\":\"km\",\"name\":\"KHMER\"},{\"code\":\"kn\",\"name\":\"KANNADA\"},{\"code\":\"ko\",\"name\":\"KOREAN\"},{\"code\":\"ks\",\"name\":\"KASHMIRI\"},{\"code\":\"ku\",\"name\":\"KURDISH\"},{\"code\":\"ky\",\"name\":\"KYRGYZ\"},{\"code\":\"la\",\"name\":\"LATIN\"},{\"code\":\"lb\",\"name\":\"LUXEMBOURGISH\"},{\"code\":\"lg\",\"name\":\"GANDA\"},{\"code\":\"lif\",\"name\":\"LIMBU\"},{\"code\":\"ln\",\"name\":\"LINGALA\"},{\"code\":\"lo\",\"name\":\"LAOTHIAN\"},{\"code\":\"lt\",\"name\":\"LITHUANIAN\"},{\"code\":\"lv\",\"name\":\"LATVIAN\"},{\"code\":\"mfe\",\"name\":\"MAURITIAN_CREOLE\"},{\"code\":\"mg\",\"name\":\"MALAGASY\"},{\"code\":\"mi\",\"name\":\"MAORI\"},{\"code\":\"mk\",\"name\":\"MACEDONIAN\"},{\"code\":\"ml\",\"name\":\"MALAYALAM\"},{\"code\":\"mn\",\"name\":\"MONGOLIAN\"},{\"code\":\"mr\",\"name\":\"MARATHI\"},{\"code\":\"ms\",\"name\":\"MALAY\"},{\"code\":\"mt\",\"name\":\"MALTESE\"},{\"code\":\"my\",\"name\":\"BURMESE\"},{\"code\":\"na\",\"name\":\"NAURU\"},{\"code\":\"ne\",\"name\":\"NEPALI\"},{\"code\":\"nl\",\"name\":\"DUTCH\"},{\"code\":\"no\",\"name\":\"NORWEGIAN\"},{\"code\":\"nr\",\"name\":\"NDEBELE\"},{\"code\":\"nso\",\"name\":\"PEDI\"},{\"code\":\"ny\",\"name\":\"NYANJA\"},{\"code\":\"oc\",\"name\":\"OCCITAN\"},{\"code\":\"om\",\"name\":\"OROMO\"},{\"code\":\"or\",\"name\":\"ORIYA\"},{\"code\":\"pa\",\"name\":\"PUNJABI\"},{\"code\":\"pl\",\"name\":\"POLISH\"},{\"code\":\"ps\",\"name\":\"PASHTO\"},{\"code\":\"pt\",\"name\":\"PORTUGUESE\"},{\"code\":\"qu\",\"name\":\"QUECHUA\"},{\"code\":\"rm\",\"name\":\"RHAETO_ROMANCE\"},{\"code\":\"rn\",\"name\":\"RUNDI\"},{\"code\":\"ro\",\"name\":\"ROMANIAN\"},{\"code\":\"ru\",\"name\":\"RUSSIAN\"},{\"code\":\"rw\",\"name\":\"KINYARWANDA\"},{\"code\":\"sa\",\"name\":\"SANSKRIT\"},{\"code\":\"sco\",\"name\":\"SCOTS\"},{\"code\":\"sd\",\"name\":\"SINDHI\"},{\"code\":\"sg\",\"name\":\"SANGO\"},{\"code\":\"si\",\"name\":\"SINHALESE\"},{\"code\":\"sk\",\"name\":\"SLOVAK\"},{\"code\":\"sl\",\"name\":\"SLOVENIAN\"},{\"code\":\"sm\",\"name\":\"SAMOAN\"},{\"code\":\"sn\",\"name\":\"SHONA\"},{\"code\":\"so\",\"name\":\"SOMALI\"},{\"code\":\"sq\",\"name\":\"ALBANIAN\"},{\"code\":\"sr\",\"name\":\"SERBIAN\"},{\"code\":\"ss\",\"name\":\"SISWANT\"},{\"code\":\"st\",\"name\":\"SESOTHO\"},{\"code\":\"su\",\"name\":\"SUNDANESE\"},{\"code\":\"sv\",\"name\":\"SWEDISH\"},{\"code\":\"sw\",\"name\":\"SWAHILI\"},{\"code\":\"syr\",\"name\":\"SYRIAC\"},{\"code\":\"ta\",\"name\":\"TAMIL\"},{\"code\":\"te\",\"name\":\"TELUGU\"},{\"code\":\"tg\",\"name\":\"TAJIK\"},{\"code\":\"th\",\"name\":\"THAI\"},{\"code\":\"ti\",\"name\":\"TIGRINYA\"},{\"code\":\"tk\",\"name\":\"TURKMEN\"},{\"code\":\"tl\",\"name\":\"TAGALOG\"},{\"code\":\"tlh\",\"name\":\"KLINGON\"},{\"code\":\"tn\",\"name\":\"TSWANA\"},{\"code\":\"to\",\"name\":\"TONGA\"},{\"code\":\"tr\",\"name\":\"TURKISH\"},{\"code\":\"ts\",\"name\":\"TSONGA\"},{\"code\":\"tt\",\"name\":\"TATAR\"},{\"code\":\"ug\",\"name\":\"UIGHUR\"},{\"code\":\"uk\",\"name\":\"UKRAINIAN\"},{\"code\":\"ur\",\"name\":\"URDU\"},{\"code\":\"uz\",\"name\":\"UZBEK\"},{\"code\":\"ve\",\"name\":\"VENDA\"},{\"code\":\"vi\",\"name\":\"VIETNAMESE\"},{\"code\":\"vo\",\"name\":\"VOLAPUK\"},{\"code\":\"war\",\"name\":\"WARAY_PHILIPPINES\"},{\"code\":\"wo\",\"name\":\"WOLOF\"},{\"code\":\"xh\",\"name\":\"XHOSA\"},{\"code\":\"yi\",\"name\":\"YIDDISH\"},{\"code\":\"yo\",\"name\":\"YORUBA\"},{\"code\":\"za\",\"name\":\"ZHUANG\"},{\"code\":\"zh\",\"name\":\"CHINESE_SIMPLIFIED\"},{\"code\":\"zh-Hant\",\"name\":\"CHINESE_TRADITIONAL\"},{\"code\":\"zu\",\"name\":\"ZULU\"}]";
        Type languageListType = new TypeToken<List<Language>>() {}.getType();
        List<Language> languages = new Gson().fromJson(languagesList, languageListType);

        // Find the language in the list with the same code as the detected language and convert it to JSON format
        Gson gson = new Gson();
        String json = "";
        for (Language lang1 : languages) {
            if (lang1.getCode().equals(lang))
                json = gson.toJson(lang1);
        }
        // Return the detected language in JSON format
        return json;


    }
    //This code is a method named translateText that takes three parameters:
    // textToTranslate, fromLang, and toLang. The purpose of the method is to
    // translate the input textToTranslate from one language (fromLang)
    // to another language (toLang).
    //
    //The code uses the MyMemory API for translation.
    // The API takes a GET request with query parameters
    // langpair, q, mt, and onlyprivate, and returns a JSON
    // response with the translated text.
    public String translateText(String textToTranslate, String fromLang, String toLang) throws InterruptedException, UnsupportedEncodingException, IOException, JsonSyntaxException, JsonIOException {

        // Create a GET request with the necessary parameters for the MyMemory API
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://translated-mymemory---translation-memory.p.rapidapi.com/get?langpair=" + fromLang + "%7C" + toLang + "&q=" + URLEncoder.encode(textToTranslate, "UTF-8") + "&mt=1&onlyprivate=0&de=a%40b.c"))
                .header("X-RapidAPI-Key", "75a6e40a80mshde7c35f1ff6bda4p13e47cjsn67887f0075fd")
                .header("X-RapidAPI-Host", "translated-mymemory---translation-memory.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the GET request and retrieve the response from the MyMemory API
        HttpResponse<String> response3 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response from MyMemory API: " + response3.body());

        // Parse the response as JSON to retrieve the translated text
        JsonObject jsonObject = new JsonParser().parse(response3.body().toString()).getAsJsonObject();
        String responseData = String.valueOf(jsonObject.get("responseData"));
        JsonObject jsonObject2 = new JsonParser().parse(responseData).getAsJsonObject();

        return jsonObject2.toString();

    }

    /*
    The purpose of the above function is to fetch data from a MongoDB database,
    specifically the "TranslateDB" database and the "translateServer" collection,
    and store it in an ArrayList. The function reads the documents in the collection,
    converts them to JSON objects, extracts specific fields from the objects,
    and appends them to a HTML table. It also retrieves the detectAvgTime and
    translateAvgTime values from the objects and adds them to the ArrayList.
    Finally, the function returns the ArrayList with the tableValues, detectAvgTime, and translateAvgTime.
     */
    public ArrayList<String> fetchFromMongoDB() {
        // Set the log level of the MongoDB driver to ERROR to avoid log clutter
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
        // Initialize variables
        String tableValues = "";
        ArrayList<String> res = new ArrayList();
        String detectAvgTime = "";
        String translateAvgTime = "";

        // MongoDB connection URI
        String uri = "mongodb connection uri";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Connect to the database and retrieve the collection
            MongoDatabase db = mongoClient.getDatabase("TranslateDB");
            MongoCollection<Document> c = db.getCollection("translateServer");
            // Query the collection and retrieve all documents
            MongoCursor<Document> cursor = c.find().iterator();
            System.out.println("Reading from DB");
            Gson gson = new Gson();
            // Loop through each document and extract relevant information
            while (cursor.hasNext()) {
                String report = cursor.next().toJson();
                // Parse the JSON document into a JsonObject and extract values
                JsonParser jsonParser = new JsonParser();
                JsonElement element = jsonParser.parse(report);
                JsonObject object = gson.fromJson(element, JsonObject.class);
                // Build a HTML table row string with document values
                tableValues += "<tr><td>"+ object.get("date")+"</td><td>"+object.get("browser")+"</td><td>"+object.get("device")+"</td><td>"+object.get("textToDetect")+"</td><td>"+object.get("detectedLang")+"</td><td>"+object.get("convertedText")+"</td>";
                detectAvgTime = String.valueOf(object.get("detectAvgTime"));
                translateAvgTime = String.valueOf(object.get("translateAvgTime"));
            }

        }
        // Add extracted information to the result ArrayList
        res.add(tableValues);
        res.add(detectAvgTime);
        res.add(translateAvgTime);
        return res;
    }
}
