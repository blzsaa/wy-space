package hu.blzsaa.wyspace;

import hu.blzsaa.wyspace.dto.PassDto;

public class PassDtoBuilder {
  private String name;
  private int strength;
  private int startTime;
  private int endTime;

  public PassDtoBuilder name(String name) {
    this.name = name;
    return this;
  }

  public PassDtoBuilder strength(int strength) {
    this.strength = strength;
    return this;
  }

  public PassDtoBuilder startTime(int startTime) {
    this.startTime = startTime;
    return this;
  }

  public PassDtoBuilder endTime(int endTime) {
    this.endTime = endTime;
    return this;
  }

  public PassDto build() {
    PassDto passDto = new PassDto();
    passDto.setName(name);
    passDto.setStrength(strength);
    passDto.setStartTime(startTime);
    passDto.setEndTime(endTime);
    return passDto;
  }
}
