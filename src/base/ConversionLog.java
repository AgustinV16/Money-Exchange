package base;

import java.time.LocalDateTime;

public record ConversionLog(String from, String to, String amount, String result, LocalDateTime timestamp) {}
