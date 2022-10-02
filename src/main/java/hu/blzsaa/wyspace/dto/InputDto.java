package hu.blzsaa.wyspace.dto;

import java.io.InputStream;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record InputDto(@NotNull InputStream inputStream, @Min(0) long bandwidth) {}
