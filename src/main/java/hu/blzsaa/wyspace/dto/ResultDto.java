package hu.blzsaa.wyspace.dto;

import java.time.LocalTime;

public record ResultDto(LocalTime startTime, LocalTime endTime, boolean downloadable) {}
