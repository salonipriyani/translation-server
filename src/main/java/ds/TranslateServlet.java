package ds;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.RequestDispatcher;

import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.google.gson.JsonObject;


import com.mongodb.client.*;
import org.bson.Document;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

//Name: Saloni Priyani
// Andrew ID: spriyani

/**
 * This class is a Java servlet that provides an API for translating text.
 * It responds to HTTP GET requests to the endpoints /detectLanguage,
 * /translate, and /getDashboard. The servlet uses the TranslateModel class,
 * which contains the business logic for the application. The servlet also
 * logs usage statistics and sends them to a MongoDB database.
 */
@WebServlet(name = "TranslateServlet",
        urlPatterns = {"/detectLanguage", "/translate", "/getDashboard"})
public class TranslateServlet extends HttpServlet {

    TranslateModel tm = null;  // The "business model" for this app
    List<Double> detectLangApiTime;
    List<Double> translateApiTime;
    Map<String, Integer> browserMap;
    double averageDetectLang;
    double averageTranslate;

    // The init() method is called when the servlet is initialized.
    // It instantiates the TranslateModel class and initializes the instance variables.
    @Override
    public void init() {
        tm = new TranslateModel();
        detectLangApiTime = new ArrayList<>();
        translateApiTime = new ArrayList<>();
        browserMap = new HashMap<>();
        browserMap.put("Firefox", 0);
        browserMap.put("Explorer", 0);
        browserMap.put("Safari", 0);
        browserMap.put("Chrome", 0);
        browserMap.put("Direct API access", 0);
        averageDetectLang = 0.0;
        averageTranslate = 0.0;
    }

    // This servlet will reply to HTTP GET requests via this doGet method
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String ua = request.getHeader("User-Agent");

        boolean mobile;
        // prepare the appropriate DOCTYPE for the view pages
        if (ua != null && ((ua.indexOf("Android") != -1) || (ua.indexOf("iPhone") != -1))) {
            mobile = true;
            /*
             * This is the latest XHTML Mobile doctype. To see the difference it
             * makes, comment it out so that a default desktop doctype is used
             * and view on an Android or iPhone.
             */
            request.setAttribute("doctype", "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.2//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile12.dtd\">");
        } else {
            mobile = false;
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
        }

        String device;
        String browser;

        //he method also determines the type of browser and device that made
        // the request and updates the browserMap accordingly. It then sorts
        // the browserMap by the number of requests made by each type of
        // browser and sets the sorted list as a request attribute.

        if (ua.contains("Firefox"))
            browser = "Firefox";
        else if (ua.contains("Explorer"))
            browser = "Internet Explorer";
        else if (ua.contains("Safari"))
            browser = "Safari";
        else if (ua.contains("Chrome"))
            browser = "Chrome";
        else
            browser = "Direct API access";

        if (ua.contains("Android"))
            device = "Android";
        else if (ua.contains("Windows"))
            device = "Windows";
        else if (ua.contains("Mac"))
            device = "Mac";
        else
            device = "Other device";

        browserMap.put(browser, browserMap.get(browser) + 1);
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(browserMap.entrySet());
        Collections.sort(entryList, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            keyList.add(entry.getKey());
        }
        request.setAttribute("descBrowserList", keyList);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        System.out.println(request.getRequestURI());

        // If the request URI is /detectLanguage or /translate, the method gets the
        // textToDetect or textToTranslate parameter from the request, respectively.
        // It then calls the appropriate method of the TranslateModel class
        // (detectLang() or translate()) to detect the language of the text or to
        // translate the text. If the textToDetect parameter is empty, the method
        // throws an InvalidInputException. If the method call is successful, the
        // method calculates the time it took to make the API call and adds it
        // to the appropriate list. It then calculates the average time for API calls
        // of that type and sets the result as a request attribute
        if (request.getRequestURI().equals("/TranslationServer-1.0-SNAPSHOT/detectLanguage") || request.getRequestURI().equals("/detectLanguage")) {
            String textToDetect = request.getParameter("textToDetect");

            String detectResponse = null;

            try {
                if(textToDetect.isEmpty()) {
                    throw new InvalidInputException("Text to detect cannot be empty");
                }
                System.out.println(textToDetect);

                detectResponse = tm.detectLang(textToDetect);

                // Set the content type of the response to indicate that we're sending JSON
                response.setContentType("application/json");
                JsonObject jsonObject = JsonParser.parseString(detectResponse).getAsJsonObject();
                String detectedLang = String.valueOf(jsonObject.get("name"));
                long endTime = System.currentTimeMillis();
                // calculate time taken in seconds
                double timeTaken = (endTime - startTime) / 1000.0;
                detectLangApiTime.add(timeTaken);
                double sum = 0;

                for (Double value : detectLangApiTime) {
                    sum += value;
                }
                System.out.println(sum);
                System.out.println(detectLangApiTime);
                averageDetectLang = sum / detectLangApiTime.size();
                System.out.println(averageDetectLang);

                request.setAttribute("averageDetectLang", String.valueOf(averageDetectLang));
                sendToMongoDB(formatter.format(date), browser, device, textToDetect, detectedLang,"", averageDetectLang, averageTranslate);
            }
            catch (InvalidInputException e) {
                // handle the exception by returning an error message to the user
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
            catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
            catch (JsonSyntaxException | JsonIOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }

            // Write the JSON data to the response output stream
            PrintWriter out = response.getWriter();
            out.print(detectResponse);
            out.flush();
        }
        else if (request.getRequestURI().equals("/TranslationServer-1.0-SNAPSHOT/translate") || request.getRequestURI().equals("/translate")) {
            System.out.println("hello");
            String fromLang = request.getParameter("fromLang");
            String toLang = request.getParameter("toLang");
            String textToTranslate = request.getParameter("textToTranslate");
            String translateResponse = null;
            try {
                if(textToTranslate.isEmpty() || fromLang.isEmpty()  || toLang.isEmpty() ) {
                    throw new InvalidInputException("Input cannot be empty");
                }


                translateResponse = "";
                try {
                    translateResponse = tm.translateText(textToTranslate, fromLang, toLang);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }



                // Set the content type of the response to indicate that we're sending JSON
                response.setContentType("application/json");
                JsonObject jsonObject = JsonParser.parseString(translateResponse).getAsJsonObject();
                String translatedText = String.valueOf(jsonObject.get("translatedText"));
                long endTime = System.currentTimeMillis();
                // calculate time taken in seconds
                double timeTaken = (endTime - startTime) / 1000.0;
                translateApiTime.add(timeTaken);
                double sum = 0;
                for (Double value : translateApiTime) {
                    sum += value;
                }
                System.out.println(sum);
                System.out.println(translateApiTime);
                averageTranslate = sum / translateApiTime.size();
                request.setAttribute("averageTranslate", String.valueOf(averageTranslate));
                System.out.println(averageTranslate);
                sendToMongoDB(formatter.format(date), browser, device, textToTranslate, "",translatedText, averageDetectLang, averageTranslate);
            }
            catch (InvalidInputException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
            catch (IOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }
            catch (JsonSyntaxException | JsonIOException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
            }

            // Write the JSON data to the response output stream
            PrintWriter out = response.getWriter();
            out.print(translateResponse.toString());
            out.flush();
        }
        //fetches data from a MongoDB database and sets request attributes
        // before forwarding the request and response to the dashboard JSP file for rendering.
        else if (request.getRequestURI().equals("/TranslationServer-1.0-SNAPSHOT/getDashboard") || request.getServletPath().equals("/getDashboard")) {

            ArrayList<String> res = tm.fetchFromMongoDB();

            request.setAttribute("logs", res.get(0));
            request.setAttribute("averageDetectLang", res.get(1));
            request.setAttribute("averageTranslate", res.get(2));

            RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
            view.forward(request, response);
        }

    }

    //This function sends the translation data to MongoDB server.
    private void sendToMongoDB(String date, String browser, String device, String textToDetect, String detectedLang, String convertedText, double detectAvgTime, double translateAvgTime) {
        // Sets the logging level for MongoDB driver to ERROR
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
        // URI for connecting to the MongoDB server
        String uri = "<replace>";
        // Creates a new MongoDB client using the given URI
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // Gets the "TranslateDB" database from the MongoDB client
            MongoDatabase db = mongoClient.getDatabase("TranslateDB");
            // Gets the "translateServer" collection from the "TranslateDB" database
            MongoCollection<Document> c = db.getCollection("translateServer");
            // Creates a new JSON object to store the translation data
            JsonObject log = new JsonObject();

            // Adds the translation data to the JSON object
            log.addProperty("date", date);
            log.addProperty("browser", browser);
            log.addProperty("device", device);
            log.addProperty("textToDetect", textToDetect);
            log.addProperty("detectedLang", detectedLang);
            log.addProperty("convertedText", convertedText);
            log.addProperty("detectAvgTime", detectAvgTime);
            log.addProperty("translateAvgTime", translateAvgTime);

            // Converts the JSON object to a MongoDB document
            Document document = new Document(Document.parse(log.toString()));
            // Inserts the MongoDB document into the "translateServer" collection
            c.insertOne(document);
        }
    }
}

