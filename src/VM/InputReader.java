package VM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class InputReader {
    private final Iterator<Scanner> lines;

    public InputReader(String filename) {
        try {
            this.lines = new BufferedReader(new FileReader(filename)).lines().map(line -> (new Scanner(line.trim())).useDelimiter(" ")).iterator();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasNext() {
        return this.lines.hasNext();
    }

    public Pair<String, ArrayList<Instruction>> nextFunction() {
        ArrayList<Instruction> func = new ArrayList<>();

        String functionName = null;
        while (this.hasNext()) {
            Scanner reader = this.lines.next();

            if (!reader.hasNext()) {
                break;
            }

            String s = reader.next().trim();
            switch (s) {
                case "ADD":
                    func.add(new Add(reader.nextInt(), reader.nextInt(), reader.nextInt()));
                    break;
                case "SUB":
                    func.add(new Sub(reader.nextInt(), reader.nextInt(), reader.nextInt()));
                    break;
                case "MUL":
                    func.add(new Mul(reader.nextInt(), reader.nextInt(), reader.nextInt()));
                    break;
                case "DIV":
                    func.add(new Div(reader.nextInt(), reader.nextInt(), reader.nextInt()));
                    break;
                case "SET":
                    func.add(new Set(reader.nextInt(), reader.nextInt()));
                    break;
                case "MOV":
                    func.add(new Mov(reader.nextInt(), reader.nextInt()));
                    break;
                case "CALL":
                    func.add(new Call(reader.next(), reader.nextInt()));
                    break;
                case "RETURN":
                    func.add(new Return(reader.nextInt()));
                    break;
                case "INSPECT":
                    func.add(new Inspect(reader.nextInt()));
                    break;
                default:
                    assert s.endsWith(":");
                    functionName = s.substring(0, s.length() - 1);
                    break;
            }
        }

        assert functionName != null;

        return new Pair<>(functionName, func);
    }
}
