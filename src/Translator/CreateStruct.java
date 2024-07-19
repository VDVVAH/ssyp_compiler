package Translator;

import Parsing.Struct;

import java.util.Arrays;
import java.util.stream.Collectors;

public class CreateStruct implements BytecodeInstruction {
    private final Struct struct_template;
    private final int structOffset;

    public CreateStruct(Struct struct_template, int structOffset) {
        this.struct_template = struct_template;
        this.structOffset = structOffset;
    }

    @Override
    public BytecodeType kind() {
        return BytecodeType.CREATE_STRUCT;
    }

    @Override
    public String toString() {
        return String.format("CREATE_STRUCT %d %s", structOffset,
                Arrays.stream(struct_template.fields()).map(field -> field.type()[0].equals("Int") ? "0": field.type()[1]).collect(Collectors.joining(" ")));
    }
}
