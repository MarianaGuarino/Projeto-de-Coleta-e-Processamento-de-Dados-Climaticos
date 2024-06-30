package MarianaPCDProject;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class WeatherDataCollector {

    private static final String API_URL = "https://api.open-meteo.com/v1/forecast";
    private static final String[] CAPITAIS = {
            "Aracaju", "Belem","Belo Horizonte","Boa Vista", "Brasilia",
            "Campo Grande", "Cuiaba", "Curitiba", "Florianopolis", "Fortaleza",
            "Goiania", "Joao Pessoa", "Macapa", "Maceio", "Manaus", "Natal",
            "Palmas", "Porto Velho", "Recife", "Rio Branco", "Rio de Janeiro",
            "Salvador", "Sao Luis", "Sao Paulo", "Teresina", "Vitoria"
    };

    private static final int NUM_DIAS = 7;

    private final HttpClient httpClient;
    private final Gson gson;
    private final Map<String, CityWeatherData> dadosClimaticosPorCidade;

    public WeatherDataCollector() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
        this.dadosClimaticosPorCidade = new HashMap<>();
    }

    public void coletarEProcessarDadosSemThreads() {
        long tempoInicio = System.currentTimeMillis();

        for (String cidade : CAPITAIS) {
            try {
                double[] coordenadas = obterCoordenadasCidade(cidade);
                if (coordenadas != null) {
                    double latitude = coordenadas[0];
                    double longitude = coordenadas[1];
                    String resposta = fazerRequisicaoApi(latitude, longitude);
                    List<DadosClimaticos> listaDadosClimaticos = parsearDadosClimaticos(resposta);
                    processarDadosClimaticos(cidade, listaDadosClimaticos);
                } else {
                    System.err.println("❌ Falha ao obter coordenadas para a cidade: " + cidade);
                }
            } catch (IOException e) {
                e.fillInStackTrace();
            }
        }

        long tempoFim = System.currentTimeMillis();
        System.out.println("⏱️ Tempo total (sem threads): " + (tempoFim - tempoInicio) + " ms");
    }

    public void coletarEProcessarDadosCom3Threads() throws InterruptedException {
        long tempoInicio = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();
        int cidadesPorThread = CAPITAIS.length / 3;

        for (int i = 0; i < 3; i++) {
            int inicio = i * cidadesPorThread;
            int fim = (i + 1) * cidadesPorThread;

            Thread thread = new Thread(() -> {
                for (int j = inicio; j < fim; j++) {
                    String cidade = CAPITAIS[j];
                    try {
                        double[] coordenadas = obterCoordenadasCidade(cidade);
                        if (coordenadas != null) {
                            double latitude = coordenadas[0];
                            double longitude = coordenadas[1];
                            String resposta = fazerRequisicaoApi(latitude, longitude);
                            List<DadosClimaticos> listaDadosClimaticos = parsearDadosClimaticos(resposta);
                            processarDadosClimaticos(cidade, listaDadosClimaticos);
                        } else {
                            System.err.println("❌ Falha ao obter coordenadas para a cidade: " + cidade);
                        }
                    } catch (IOException e) {
                        e.fillInStackTrace();
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long tempoFim = System.currentTimeMillis();
        System.out.println("⏱️ Tempo total (3 threads): " + (tempoFim - tempoInicio) + " ms");
    }

    public void coletarEProcessarDadosCom9Threads() throws InterruptedException {
        long tempoInicio = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();
        int cidadesPorThread = 3;

        for (int i = 0; i < CAPITAIS.length; i += cidadesPorThread) {
            final int inicio = i;
            Thread thread = new Thread(() -> {
                for (int j = inicio; j < inicio + cidadesPorThread && j < CAPITAIS.length; j++) {
                    String cidade = CAPITAIS[j];
                    try {
                        double[] coordenadas = obterCoordenadasCidade(cidade);
                        if (coordenadas != null) {
                            double latitude = coordenadas[0];
                            double longitude = coordenadas[1];
                            String resposta = fazerRequisicaoApi(latitude, longitude);
                            List<DadosClimaticos> listaDadosClimaticos = parsearDadosClimaticos(resposta);
                            processarDadosClimaticos(cidade, listaDadosClimaticos);
                        } else {
                            System.err.println("❌ Falha ao obter coordenadas para a cidade: " + cidade);
                        }
                    } catch (IOException e) {
                        e.fillInStackTrace();
                    }
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long tempoFim = System.currentTimeMillis();
        System.out.println("⏱️ Tempo total (9 threads): " + (tempoFim - tempoInicio) + " ms");
    }

    public void coletarEProcessarDadosCom27Threads() throws InterruptedException {
        long tempoInicio = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>();

        for (String cidade : CAPITAIS) {
            Thread thread = new Thread(() -> {
                try {
                    double[] coordenadas = obterCoordenadasCidade(cidade);
                    if (coordenadas != null) {
                        double latitude = coordenadas[0];
                        double longitude = coordenadas[1];
                        String resposta = fazerRequisicaoApi(latitude, longitude);
                        List<DadosClimaticos> listaDadosClimaticos = parsearDadosClimaticos(resposta);
                        processarDadosClimaticos(cidade, listaDadosClimaticos);
                    } else {
                        System.err.println("❌ Falha ao obter coordenadas para a cidade: " + cidade);
                    }
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            });

            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long tempoFim = System.currentTimeMillis();
        System.out.println("⏱️ Tempo total (27 threads): " + (tempoFim - tempoInicio) + " ms");
    }

    private void processarDadosClimaticos(String cidade, List<DadosClimaticos> listaDadosClimaticos) {
        CityWeatherData dadosCidade = new CityWeatherData(cidade);

        for (int i = 0; i < NUM_DIAS; i++) {
            DadosClimaticos dadosClimaticos = listaDadosClimaticos.get(i);
            dadosCidade.setTemperaturaMinima(i + 1, dadosClimaticos.getTemperaturaMinima());
            dadosCidade.setTemperaturaMaxima(i + 1, dadosClimaticos.getTemperaturaMaxima());
            dadosCidade.setTemperaturaMedia(i + 1, dadosClimaticos.getTemperaturaMedia());
        }

        System.out.println("🌆 Cidade: " + cidade);
        for (int dia = 1; dia <= NUM_DIAS; dia++) {
            System.out.println("📅 Dia " + dia + ":");
            System.out.println("🌡️ Temperatura mínima: " + dadosCidade.getTemperaturaMinima(dia) + "°C");
            System.out.println("🌡️ Temperatura máxima: " + dadosCidade.getTemperaturaMaxima(dia) + "°C");
            System.out.println("🌡️ Temperatura média: " + dadosCidade.getTemperaturaMedia(dia) + "°C");
        }
        System.out.println();
    }

    private String fazerRequisicaoApi(double latitude, double longitude) throws IOException {
        String apiUrl = API_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&daily=temperature_2m_min,temperature_2m_max,temperature_2m_mean";
        HttpGet request = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new IOException("Falha na requisição HTTP: " + statusCode);
        }

        return EntityUtils.toString(response.getEntity());
    }

    private List<DadosClimaticos> parsearDadosClimaticos(String resposta) {
        List<DadosClimaticos> listaDadosClimaticos = new ArrayList<>();

        JsonObject jsonObject = gson.fromJson(resposta, JsonObject.class);
        JsonObject daily = jsonObject.getAsJsonObject("daily");

        JsonArray temperaturasMin = daily.getAsJsonArray("temperature_2m_min");
        JsonArray temperaturasMax = daily.getAsJsonArray("temperature_2m_max");
        JsonArray temperaturasMed = daily.getAsJsonArray("temperature_2m_mean");

        for (int i = 0; i < temperaturasMin.size(); i++) {
            double tempMin = temperaturasMin.get(i).getAsDouble();
            double tempMax = temperaturasMax.get(i).getAsDouble();
            double tempMed = temperaturasMed.get(i).getAsDouble();

            listaDadosClimaticos.add(new DadosClimaticos(tempMin, tempMax, tempMed));
        }

        return listaDadosClimaticos;
    }

    private double[] obterCoordenadasCidade(String cidade) {
        return switch (cidade) {
            case "Natal" -> new double[]{-5.7833, -35.2};
            case "Manaus" -> new double[]{-3.1189, -60.0217};
            case "Sao Luis" -> new double[]{-2.5283, -44.3044};
            case "Cuiaba" -> new double[]{-15.5989, -56.0949};
            case "Recife" -> new double[]{-8.05, -34.9};
            case "Curitiba" -> new double[]{-25.4297, -49.2711};
            case "Florianopolis" -> new double[]{-27.5935, -48.55854};
            case "Fortaleza" -> new double[]{-3.7275, -38.5275};
            case "Belo Horizonte" -> new double[]{-19.9167, -43.9333};
            case "Porto Alegre" -> new double[]{-30.0331, -51.23};
            case "Campo Grande" -> new double[]{-20.44278, -54.64639};
            case "Aracaju" -> new double[]{-10.9167, -37.05};
            case "Porto Velho" -> new double[]{-8.76194, -63.90389};
            case "Goiania" -> new double[]{-16.67861, -49.25389};
            case "Brasilia" -> new double[]{-15.7939, -47.8828};
            case "Belem" -> new double[]{-1.4558, -48.5039};
            case "Rio Branco" -> new double[]{-9.97472, -67.81};
            case "Salvador" -> new double[]{-12.9747, -38.4767};
            case "Boa Vista" -> new double[]{2.81972, -60.67333};
            case "Palmas" -> new double[]{-10.16745, -48.32766};
            case "Rio de Janeiro" -> new double[]{-22.9111, -43.2056};
            case "Maceio" -> new double[]{-9.66583, -35.73528};
            case "Macapa" -> new double[]{0.033, -51.05};
            case "Joao Pessoa" -> new double[]{-7.12, -34.88};
            case "Sao Paulo" -> new double[]{-23.55, -46.6333};
            case "Teresina" -> new double[]{-5.089167, -42.801944};
            case "Vitoria" -> new double[]{-20.2889, -40.3083};
            default -> null;
        };
    }

    public void gerarArquivoExcel(String caminhoArquivo, String pastaDestino) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Dados Climáticos");

            int rowNum = 0;
            for (CityWeatherData cidadeData : dadosClimaticosPorCidade.values()) {
                Row rowCidade = sheet.createRow(rowNum++);
                Cell cellCidade = rowCidade.createCell(0);
                cellCidade.setCellValue(cidadeData.getCidade());

                for (int dia = 1; dia <= NUM_DIAS; dia++) {
                    Row rowDia = sheet.createRow(rowNum++);
                    Cell cellDia = rowDia.createCell(0);
                    cellDia.setCellValue("Dia " + dia);

                    Cell cellMin = rowDia.createCell(1);
                    cellMin.setCellValue(cidadeData.getTemperaturaMinima(dia));

                    Cell cellMax = rowDia.createCell(2);
                    cellMax.setCellValue(cidadeData.getTemperaturaMaxima(dia));

                    Cell cellMedia = rowDia.createCell(3);
                    cellMedia.setCellValue(cidadeData.getTemperaturaMedia(dia));
                }
            }

            // Verificar se a pasta de destino existe; se não, criar
            File outputFolder = new File(pastaDestino);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }

            // Caminho completo do arquivo
            String caminhoCompleto = pastaDestino + File.separator + caminhoArquivo;

            try (FileOutputStream outputStream = new FileOutputStream(caminhoCompleto)) {
                workbook.write(outputStream);
            }

            System.out.println("✅ Arquivo Excel gerado com sucesso em: " + caminhoCompleto);
        } catch (IOException e) {
            System.err.println("❌ Erro ao gerar arquivo Excel: " + e.getMessage());
        }
    }


    private static class DadosClimaticos {
        private final double temperaturaMinima;
        private final double temperaturaMaxima;
        private final double temperaturaMedia;

        public DadosClimaticos(double temperaturaMinima, double temperaturaMaxima, double temperaturaMedia) {
            this.temperaturaMinima = temperaturaMinima;
            this.temperaturaMaxima = temperaturaMaxima;
            this.temperaturaMedia = temperaturaMedia;
        }

        public double getTemperaturaMinima() {
            return temperaturaMinima;
        }

        public double getTemperaturaMaxima() {
            return temperaturaMaxima;
        }

        public double getTemperaturaMedia() {
            return temperaturaMedia;
        }
    }

    private static class CityWeatherData {
        private final String cidade;
        private final Map<Integer, Double> temperaturaMinimaPorDia;
        private final Map<Integer, Double> temperaturaMaximaPorDia;
        private final Map<Integer, Double> temperaturaMediaPorDia;

        public CityWeatherData(String cidade) {
            this.cidade = cidade;
            this.temperaturaMinimaPorDia = new HashMap<>();
            this.temperaturaMaximaPorDia = new HashMap<>();
            this.temperaturaMediaPorDia = new HashMap<>();
        }

        public String getCidade() {
            return cidade;
        }

        public void setTemperaturaMinima(int dia, double temperaturaMinima) {
            this.temperaturaMinimaPorDia.put(dia, temperaturaMinima);
        }

        public void setTemperaturaMaxima(int dia, double temperaturaMaxima) {
            this.temperaturaMaximaPorDia.put(dia, temperaturaMaxima);
        }

        public void setTemperaturaMedia(int dia, double temperaturaMedia) {
            this.temperaturaMediaPorDia.put(dia, temperaturaMedia);
        }

        public double getTemperaturaMinima(int dia) {
            return temperaturaMinimaPorDia.get(dia);
        }

        public double getTemperaturaMaxima(int dia) {
            return temperaturaMaximaPorDia.get(dia);
        }

        public double getTemperaturaMedia(int dia) {
            return temperaturaMediaPorDia.get(dia);
        }
    }
}
