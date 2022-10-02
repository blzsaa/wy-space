package hu.blzsaa.wyspace.fileinterpreter;

import hu.blzsaa.wyspace.fileinterpreter.constraints.EndTimeHasToBeValidTime;
import hu.blzsaa.wyspace.fileinterpreter.constraints.FieldSeparatorConstraint;
import hu.blzsaa.wyspace.fileinterpreter.constraints.StartTimeHasToBeValidTime;
import hu.blzsaa.wyspace.fileinterpreter.constraints.StrengthHasToBeAPositiveNumber;
import java.util.List;

@FieldSeparatorConstraint
@StrengthHasToBeAPositiveNumber
@StartTimeHasToBeValidTime
@EndTimeHasToBeValidTime
public record FileLineDto(int lineNumber, List<String> fields) {}
