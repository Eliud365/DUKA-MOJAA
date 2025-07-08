package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

public class SimpleServer {

    private static final String DB_URL = "jdbc:sqlite:drinksales.db";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 5000), 0);

        server.createContext("/drinks", new DrinksHandler());
        server.createContext("/orders", new OrdersHandler());
        server.createContext("/", new NotFoundHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("✅ Server running on:");
        System.out.println("• Local: http://localhost:5000");
        System.out.println("• LAN:   http://<your-local-IP>:5000");
    }

    // === GET /drinks ===
    static class DrinksHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                List<Map<String, Object>> drinks = new ArrayList<>();

                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM drinks");
                     ResultSet rs = stmt.executeQuery()) {

                    while (rs.next()) {
                        Map<String, Object> drink = new HashMap<>();
                        drink.put("name", rs.getString("name"));
                        drink.put("quantity", rs.getInt("quantity"));
                        drinks.add(drink);
                    }

                    String json = toJson(drinks);
                    byte[] response = json.getBytes();
                    exchange.sendResponseHeaders(200, response.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response);
                    os.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                    respond(exchange, 500, "Database error");
                }
            } else {
                respond(exchange, 405, "Method Not Allowed");
            }
        }
    }

    // === POST /orders ===
    static class OrdersHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                addCorsHeaders(exchange);
                respond(exchange, 200, "OK");
                return;
            }

            if (!"POST".equals(exchange.getRequestMethod())) {
                respond(exchange, 405, "Method Not Allowed");
                return;
            }

            addCorsHeaders(exchange);

            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().reduce("", (acc, line) -> acc + line);

            System.out.println("📦 Incoming order: " + body);

            Map<String, String> params = parseFormData(body);

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String orderDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                String insert = "INSERT INTO orders (customerName, drinkName, quantity, branchName, totalAmount, orderDate) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insert);
                stmt.setString(1, params.get("customer"));
                stmt.setString(2, params.get("drink"));
                stmt.setInt(3, Integer.parseInt(params.get("quantity")));
                stmt.setString(4, params.get("branch"));
                stmt.setDouble(5, Double.parseDouble(params.get("total")));
                stmt.setString(6, orderDate);
                stmt.executeUpdate();

                // Reduce drink stock
                String update = "UPDATE drinks SET quantity = quantity - ? WHERE name = ?";
                PreparedStatement updateStmt = conn.prepareStatement(update);
                updateStmt.setInt(1, Integer.parseInt(params.get("quantity")));
                updateStmt.setString(2, params.get("drink"));
                updateStmt.executeUpdate();

                respond(exchange, 200, "✅ Order saved.");
            } catch (Exception e) {
                e.printStackTrace();
                respond(exchange, 500, "❌ Error saving order.");
            }
        }
    }

    // === Default handler ===
    static class NotFoundHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            respond(exchange, 404, "Route not found");
        }
    }

    // === Helper methods ===

    static void respond(HttpExchange exchange, int code, String message) throws IOException {
        addCorsHeaders(exchange);
        byte[] response = message.getBytes();
        exchange.sendResponseHeaders(code, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    static Map<String, String> parseFormData(String body) {
        Map<String, String> data = new HashMap<>();
        for (String pair : body.split("&")) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                data.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }
        return data;
    }

    static String toJson(List<Map<String, Object>> list) {
        StringBuilder json = new StringBuilder("[");
        for (Map<String, Object> map : list) {
            json.append("{");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":");
                if (entry.getValue() instanceof Number) {
                    json.append(entry.getValue());
                } else {
                    json.append("\"").append(entry.getValue()).append("\"");
                }
                json.append(",");
            }
            json.setLength(json.length() - 1); // remove trailing comma
            json.append("},");
        }
        if (!list.isEmpty()) json.setLength(json.length() - 1); // remove trailing comma
        json.append("]");
        return json.toString();
    }
}
