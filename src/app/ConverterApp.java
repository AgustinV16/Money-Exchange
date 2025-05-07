package app;

import base.ConversionLog;
import base.Currency;
import utils.CurrencyCodes;
import utils.ExchangeRateClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConverterApp {
    private final String SEPARATOR = "***************************************";
    private final Scanner scanner = new Scanner(System.in);
    private List<String> availableCurrencies = new ArrayList<>();
    private final List<ConversionLog> conversionHistory = new ArrayList<>();

    public void run() {
        System.out.println("|| Welcome to the Currency Converter ||\n");
        displayMainMenu();
    }

    private void displayMainMenu() {
        double selectedOption = -1;

        while (selectedOption != 0) {
            availableCurrencies = CurrencyCodes.getCurrencyNames();

            System.out.println(SEPARATOR + "\n");
            System.out.println(" +[ Choose a base currency to convert from ]+ ");
            printCurrencyOptions();

            System.out.println("0- Exit");
            System.out.println("H- View conversion history");

            System.out.println(SEPARATOR + "\n> Enter an option:");
            String input = scanner.next().trim();

            if (input.equalsIgnoreCase("0")) {
                selectedOption = 0;
            } else if (input.equalsIgnoreCase("H")) {
                showConversionHistory();
                continue;
            } else {
                try {
                    selectedOption = Double.parseDouble(input);
                } catch (NumberFormatException e) {
                    System.out.println("> Invalid input. Try again.\n");
                    continue;
                }

                if (selectedOption >= 1 && selectedOption <= availableCurrencies.size()) {
                    String baseCurrencyName = availableCurrencies.get((int) selectedOption - 1);
                    availableCurrencies.remove((int) selectedOption - 1);
                    displayConversionMenu(baseCurrencyName);
                } else {
                    System.out.println("> Option out of range. Try again.\n");
                }
            }
        }

        System.out.println("Thank you for using the Currency Converter.");
    }

    private void displayConversionMenu(String baseCurrencyName) {
        System.out.printf("\n +[ Converting from %s to... ]+ \n", baseCurrencyName);
        printCurrencyOptions();

        System.out.println(SEPARATOR + "\n> Enter the option: ");
        double selectedOption = readNumericInput(1, availableCurrencies.size());
        String targetCurrencyName = availableCurrencies.get((int) selectedOption - 1);

        System.out.printf("> Enter the amount of %s to convert to %s: ", baseCurrencyName, targetCurrencyName);
        double amount = readNumericInput(0.01, 999_999_999.99);

        var apiClient = new ExchangeRateClient();
        var result = apiClient.requestConversion(
                baseCurrencyName,
                CurrencyCodes.getCode(baseCurrencyName),
                targetCurrencyName,
                CurrencyCodes.getCode(targetCurrencyName),
                String.valueOf(amount)
        );

        displayConversionResult(result);
    }

    private void displayConversionResult(List<Currency> result) {
        System.out.println(SEPARATOR + "\n");

        Currency base = result.get(0);
        Currency converted = result.get(1);

        System.out.printf(" +[ %s %s (%s) equals: %s %s (%s) ]+ \n\n",
                base.value(), base.code(), base.name(),
                converted.value(), converted.code(), converted.name());

        // Log the conversion
        ConversionLog log = new ConversionLog(
                base.code(), converted.code(),
                base.value(), converted.value(),
                LocalDateTime.now()
        );
        conversionHistory.add(log);
    }

    private void showConversionHistory() {
        System.out.println(SEPARATOR + "\nConversion History:\n");

        if (conversionHistory.isEmpty()) {
            System.out.println("No conversions made yet.\n");
            return;
        }

        for (ConversionLog log : conversionHistory) {
            System.out.printf("[%s] %s %s → %s %s\n",
                    log.timestamp(), log.amount(), log.from(), log.result(), log.to());
        }
        System.out.println();
    }

    private void printCurrencyOptions() {
        for (int i = 0; i < availableCurrencies.size(); i++) {
            System.out.println((i + 1) + "- " + availableCurrencies.get(i));
        }
        System.out.println();
    }

    private double readNumericInput(double min, double max) {
        double inputNumber;
        try {
            inputNumber = scanner.nextDouble();
            if (inputNumber < min || inputNumber > max) throw new IllegalArgumentException();
        } catch (InputMismatchException | IllegalArgumentException e) {
            System.out.printf("> Please enter a valid number between %.2f and %.2f: ", min, max);
            scanner.nextLine();
            inputNumber = readNumericInput(min, max);
        }
        return inputNumber;
    }
}
