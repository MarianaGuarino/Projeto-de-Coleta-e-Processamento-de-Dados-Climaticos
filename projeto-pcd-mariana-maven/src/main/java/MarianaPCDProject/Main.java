package MarianaPCDProject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    private static final String OUTPUT_FOLDER = "resultados_experimentos";

    public static void main(String[] args) {
        WeatherDataCollector coletor = new WeatherDataCollector();

        try {
            // Criar a pasta de saída se não existir
            File outputFolder = new File(OUTPUT_FOLDER);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }

            // Rodadas do experimento
            int numRodadas = 10;

            List<Long> temposSemThreads = new ArrayList<>();
            List<Long> tempos3Threads = new ArrayList<>();
            List<Long> tempos9Threads = new ArrayList<>();
            List<Long> tempos27Threads = new ArrayList<>();

            // Versão de referência (sem threads)
            long tempoTotalSemThreads = 0;
            for (int i = 0; i < numRodadas; i++) {
                long tempoInicio = System.currentTimeMillis();
                coletor.coletarEProcessarDadosSemThreads();
                long tempoFim = System.currentTimeMillis();
                long tempoDecorrido = tempoFim - tempoInicio;
                tempoTotalSemThreads += tempoDecorrido;
                temposSemThreads.add(tempoDecorrido);
                System.out.println("🏃‍♂️ Execução " + (i + 1) + " (sem threads): " + tempoDecorrido + " ms");

                // Gerar arquivo Excel para cada rodada
                coletor.gerarArquivoExcel("resultado_sem_threads_" + (i + 1) + ".xlsx", OUTPUT_FOLDER + "/sem_threads");
            }
            long tempoMedioSemThreads = tempoTotalSemThreads / numRodadas;
            System.out.println("📊 Tempo médio (sem threads): " + tempoMedioSemThreads + " ms");

            // Gerar arquivo Excel com tempos para versão sem threads
            gerarArquivoTempos("tempos_sem_threads.xlsx", temposSemThreads, tempoMedioSemThreads, OUTPUT_FOLDER + "/sem_threads");

            // Versão com 3 threads
            long tempoTotal3Threads = 0;
            for (int i = 0; i < numRodadas; i++) {
                long tempoInicio = System.currentTimeMillis();
                coletor.coletarEProcessarDadosCom3Threads();
                long tempoFim = System.currentTimeMillis();
                long tempoDecorrido = tempoFim - tempoInicio;
                tempoTotal3Threads += tempoDecorrido;
                tempos3Threads.add(tempoDecorrido);
                System.out.println("🏃‍♂️ Execução " + (i + 1) + " (3 threads): " + tempoDecorrido + " ms");

                // Gerar arquivo Excel para cada rodada
                coletor.gerarArquivoExcel("resultado_com_3_threads_" + (i + 1) + ".xlsx", OUTPUT_FOLDER + "/com_3_threads");
            }
            long tempoMedio3Threads = tempoTotal3Threads / numRodadas;
            System.out.println("📊 Tempo médio (3 threads): " + tempoMedio3Threads + " ms");

            // Gerar arquivo Excel com tempos para versão com 3 threads
            gerarArquivoTempos("tempos_com_3_threads.xlsx", tempos3Threads, tempoMedio3Threads, OUTPUT_FOLDER + "/com_3_threads");

            // Versão com 9 threads
            long tempoTotal9Threads = 0;
            for (int i = 0; i < numRodadas; i++) {
                long tempoInicio = System.currentTimeMillis();
                coletor.coletarEProcessarDadosCom9Threads();
                long tempoFim = System.currentTimeMillis();
                long tempoDecorrido = tempoFim - tempoInicio;
                tempoTotal9Threads += tempoDecorrido;
                tempos9Threads.add(tempoDecorrido);
                System.out.println("🏃‍♂️ Execução " + (i + 1) + " (9 threads): " + tempoDecorrido + " ms");

                // Gerar arquivo Excel para cada rodada
                coletor.gerarArquivoExcel("resultado_com_9_threads_" + (i + 1) + ".xlsx", OUTPUT_FOLDER + "/com_9_threads");
            }
            long tempoMedio9Threads = tempoTotal9Threads / numRodadas;
            System.out.println("📊 Tempo médio (9 threads): " + tempoMedio9Threads + " ms");

            // Gerar arquivo Excel com tempos para versão com 9 threads
            gerarArquivoTempos("tempos_com_9_threads.xlsx", tempos9Threads, tempoMedio9Threads, OUTPUT_FOLDER + "/com_9_threads");

            // Versão com 27 threads
            long tempoTotal27Threads = 0;
            for (int i = 0; i < numRodadas; i++) {
                long tempoInicio = System.currentTimeMillis();
                coletor.coletarEProcessarDadosCom27Threads();
                long tempoFim = System.currentTimeMillis();
                long tempoDecorrido = tempoFim - tempoInicio;
                tempoTotal27Threads += tempoDecorrido;
                tempos27Threads.add(tempoDecorrido);
                System.out.println("🏃‍♂️ Execução " + (i + 1) + " (27 threads): " + tempoDecorrido + " ms");

                // Gerar arquivo Excel para cada rodada
                coletor.gerarArquivoExcel("resultado_com_27_threads_" + (i + 1) + ".xlsx", OUTPUT_FOLDER + "/com_27_threads");
            }
            long tempoMedio27Threads = tempoTotal27Threads / numRodadas;
            System.out.println("📊 Tempo médio (27 threads): " + tempoMedio27Threads + " ms");

            // Gerar arquivo Excel com tempos para versão com 27 threads
            gerarArquivoTempos("tempos_com_27_threads.xlsx", tempos27Threads, tempoMedio27Threads, OUTPUT_FOLDER + "/com_27_threads");

            // Gerar arquivo Excel da tabela de comparação de tempos
            gerarTabelaComparacao("tabela_comparacao_tempos.xlsx", numRodadas, temposSemThreads, tempos3Threads, tempos9Threads, tempos27Threads, tempoMedioSemThreads, tempoMedio3Threads, tempoMedio9Threads, tempoMedio27Threads, OUTPUT_FOLDER);


            // Exibir tabela de comparação
            System.out.println("\n📈 Tabela de Comparação de Tempos:");
            System.out.println("=====================================================================================================================");
            System.out.printf("%-15s %-15s %-15s %-15s %-15s\n", "Execução", "Sem Threads", "3 Threads", "9 Threads", "27 Threads");
            System.out.println("=====================================================================================================================");
            for (int i = 0; i < numRodadas; i++) {
                System.out.printf("%-15d %-15d %-15d %-15d %-15d\n", (i + 1), temposSemThreads.get(i), tempos3Threads.get(i), tempos9Threads.get(i), tempos27Threads.get(i));
            }
            System.out.println("=====================================================================================================================");
            System.out.printf("%-15s %-15d %-15d %-15d %-15d\n", "Média", tempoMedioSemThreads, tempoMedio3Threads, tempoMedio9Threads, tempoMedio27Threads);
            System.out.println("=====================================================================================================================");

        } catch (InterruptedException e) {
            e.fillInStackTrace();
        }
    }

    private static void gerarArquivoTempos(String nomeArquivo, List<Long> tempos, long tempoMedio, String pastaDestino) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tempos de Execução");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Execução");
            headerRow.createCell(1).setCellValue("Tempo (ms)");

            // Dados
            for (int i = 0; i < tempos.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(tempos.get(i));
            }

            // Média
            Row mediaRow = sheet.createRow(tempos.size() + 1);
            mediaRow.createCell(0).setCellValue("Média");
            mediaRow.createCell(1).setCellValue(tempoMedio);

            // Salvar o arquivo
            File outputFolder = new File(pastaDestino);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String caminhoArquivo = pastaDestino + "/" + nomeArquivo;
            try (FileOutputStream outputStream = new FileOutputStream(caminhoArquivo)) {
                workbook.write(outputStream);
                System.out.println("✅ Arquivo Excel de tempos gerado com sucesso em: " + caminhoArquivo);
            }

        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    private static void gerarTabelaComparacao(String nomeArquivo, int numRodadas, List<Long> temposSemThreads, List<Long> tempos3Threads, List<Long> tempos9Threads, List<Long> tempos27Threads, long tempoMedioSemThreads, long tempoMedio3Threads, long tempoMedio9Threads, long tempoMedio27Threads, String pastaDestino) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Tabela de Comparação de Tempos");

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Execução");
            headerRow.createCell(1).setCellValue("Sem Threads");
            headerRow.createCell(2).setCellValue("3 Threads");
            headerRow.createCell(3).setCellValue("9 Threads");
            headerRow.createCell(4).setCellValue("27 Threads");

            // Dados (continuação)
            for (int i = 0; i < numRodadas; i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(temposSemThreads.get(i));
                row.createCell(2).setCellValue(tempos3Threads.get(i));
                row.createCell(3).setCellValue(tempos9Threads.get(i));
                row.createCell(4).setCellValue(tempos27Threads.get(i));
            }

            // Média
            Row mediaRow = sheet.createRow(numRodadas + 1);
            mediaRow.createCell(0).setCellValue("Média");
            mediaRow.createCell(1).setCellValue(tempoMedioSemThreads);
            mediaRow.createCell(2).setCellValue(tempoMedio3Threads);
            mediaRow.createCell(3).setCellValue(tempoMedio9Threads);
            mediaRow.createCell(4).setCellValue(tempoMedio27Threads);

            // Ajustar largura das colunas
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Salvar o arquivo
            File outputFolder = new File(pastaDestino);
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }
            String caminhoArquivo = pastaDestino + "/" + nomeArquivo;
            try (FileOutputStream outputStream = new FileOutputStream(caminhoArquivo)) {
                workbook.write(outputStream);
                System.out.println("✅ Arquivo Excel da tabela de comparação de tempos gerado com sucesso em: " + caminhoArquivo);
            }

        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}
