## Спецификация языка .ir

### Описание языка
- .ir - это простой язык, который используется для обучения основам программирования. Он имеет небольшой набор команд и поддерживает только целочисленные вычисления.
- Алфавит .ir поддерживает все сиволы за исключением '#' '@'.
- Язык .ir требует, чтобы была хотя бы одна функция main.

### Операторы
Все ключевые слова начинаются с символа "#", символ'@' создает переменную.

- Оператор присваивания "=" присваивает значение выражения переменной. Пример: "#= y #42".
- Условный оператор "IF" и "ENDIF" выполняет блок операторов, если условие истинно. Пример #IF < y #2.
- Условный оператор "ELSE" выполняет блок операторов, если условие ложно. Пример #ELSE
- Условный оператор "ELIF" выполняет блок операторов, если условие истинно. Пример #ELIF < y #2.
- Оператор цикла "WHILE" и "ENDWHILE" позволяет сделать цикл.
- Оператор цикла "FOR" и "ENDFOR" позволяет сделать цикл для обработки массива.

### Переменные
- Декларация переменной "@", после чего прописывается тип переменной (Int, Array, String, возможно добавление своего типа через структуры), а затем пишется название переменной.
- Массив (Array) возможен из любого типа.
- Язык поддерживает только целочисленные значения.

### Выражения
.ir поддерживает следующие выражения:
- Арифметические операции: "+", "-", "*", "/".
- Операции сравнения: "=", "<>", "<", ">", "<=", ">=".
- После символа "#" и операции пишутся и их аргументы.
### Функции стандартной библиотеки
- "print" используется для вывода информации в терминал.
- "print_array" используется для вывода ячеек масива через запятую.
- "print_string" используется для вывода строки.
- "len" возвращает длину массива.

### Синтаксис
- Начало любой функции в языке .ir начинается с #F_BEGIN, заканчивается #F_END.
- Внутри функции после начала обозначается имя функции, всегда должна быть хотя бы одна функция - main.
- После идет часть с обозначением аргументов, начинается с #F_ARGS_BEGIN, заканчивается на #F_ARGS_END
- Следущий блок позволяет создать локальные переменные, начинается с #F_VARS_BEGIN, заканчивается на #F_VARS_END
- Далее идет основной блок функции, куда можно записывать действий функции обозначается : #F_BODY_BEGIN, заканчивается #F_BODY_END
- #S_BEGIN инициализирует начало создания структуры, #S_END обозночает конец.
- Вызов функции осуществляется так: #name_function result #x;
- "//" используются для обозначения коментариев.




### Пример программы со структурами
```
#S_BEGIN;
    #S_NAME BasicStruct;
    #S_VARS_BEGIN;
        @Int x;
        @Int y;
    #S_VARS_END;
#S_END;

#F_BEGIN;
    #F_NAME main;
    #F_ARGS_BEGIN;

    #F_ARGS_END;
    #F_VARS_BEGIN;
        @BasicStruct struct;
        @Int tmp;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #<- struct x #1;
        #<- struct y #5;
        #-> struct y tmp;

        #print_array #1 struct;
        #F_RETURN tmp;
    #F_BODY_END;
#F_END;
```


### Пример программы вычесления чисел фибоначчи  с помощью цикла while
```
#F_BEGIN;
    #F_NAME fib;    //функции присваивается имя fib
    #F_ARGS_BEGIN;
        @Int n;
    #F_ARGS_END;
    #F_VARS_BEGIN;
        @Int step-2;
        @Int step-1;
        @Int step;
        @Int help_step;
        @Int k;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #= step #1;
        #= step-2 #0;
        #= step-1 #1;
        #= k #0;
        #IF = n #0;
            #F_RETURN step-2;
        #ELIF = n #1;
            #F_RETURN step-1;
        #ELSE;
            #WHILE < k n;
                #+ k k #1;
                #= help_step step;
                #+ step step-1 step-2;
                #= step-2 step-1;
                #= step-1 help_step;
            #ENDWHILE;
        #ENDIF;
        #F_RETURN step;
    #F_BODY_END;
#F_END;

#F_BEGIN;
    #F_NAME main;
    #F_VARS_BEGIN;
        @Int result;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #fib result #11;
        #F_RETURN result;
    #F_BODY_END;
#F_END;
```
### Пример выполнения программы рекурсии
```

#F_BEGIN;
    #F_NAME recursion;

    #F_ARGS_BEGIN;
        @Int x;
        @Int y;
    #F_ARGS_END;

    #F_VARS_BEGIN;
    #F_VARS_END;

    #F_BODY_BEGIN;
        #IF < y #0;
            #* x x x;
            #F_RETURN x;
        #ELSE;
            #print #2;
            #* x x x;
            #- y y #1;
            #recursion x x y;
            #F_RETURN x;
        #ENDIF;
    #F_BODY_END;
#F_END;

#F_BEGIN;
    #F_NAME main;
    #F_ARGS_BEGIN;

    #F_ARGS_END;
    #F_VARS_BEGIN;
        @Int x;
    #F_VARS_END;

    #F_BODY_BEGIN;
        #recursion x #3 #2;
        #F_RETURN x;
#F_BODY_END;
#F_END;
```
### Пример выполнения программы с циклом For
```
#F_BEGIN;
#F_NAME main;
#F_VARS_BEGIN;
@Array #5 Int test_array;
@Array #5 Int test_other_array;
@Int elm;
@Int e;
@Int sum;
#F_VARS_END;
#F_BODY_BEGIN;
#= sum #0;
#[] test_array #0 #1;
#[] test_array #1 #2;
#[] test_array #2 #3;
#[] test_array #3 #4;
#[] test_array #4 #5;
#range test_other_array #0 #5 #1;
#FOR elm IN test_array;
#FOR e IN test_other_array;
#+ sum sum elm;
#+ sum sum e;
#ENDFOR;
#ENDFOR;
#F_RETURN sum;
#F_BODY_END;
#F_END;
```

### Пример выполнения программы Array flip

```
#F_BEGIN;
    #F_NAME flip;
    #F_ARGS_BEGIN;
        @Array x Int arr;
    #F_ARGS_END;
    #F_VARS_BEGIN;
        @Array arr Int arr2;
        @Int len;
        @Int half_len;
        @Int x;
        @Int x_past_mid;
        @Int tmp;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #len len arr;
        #/ half_len len #2;
        #= x #0;
        #print #4 #3 x half_len len;
        #WHILE < x half_len;
            #- x_past_mid half_len x;
            #IF == x_past_mid #0;
                #BREAK;
            #ENDIF;
            #][ arr x tmp;
            #[] arr x_past_mid tmp;
            #+ x x #1;
        #ENDWHILE;
        #F_RETURN arr2;
    #F_BODY_END;
#F_END;

#F_BEGIN;
    #F_NAME create_n_array;
    #F_ARGS_BEGIN;
        @Int n;
    #F_ARGS_END;
    #F_VARS_BEGIN;
        @Array n Int arr;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #F_RETURN arr;
    #F_BODY_END;
#F_END;

#F_BEGIN;
    #F_NAME main;
    #F_ARGS_BEGIN;
    #F_ARGS_END;
    #F_VARS_BEGIN;
        @Array #3 Int arr;
        @Int x;
    #F_VARS_END;
    #F_BODY_BEGIN;
        #create_n_array arr #5;
        #[] arr #0 #1;
        #[] arr #1 #2;
        #[] arr #2 #3;
        #[] arr #3 #4;
        #[] arr #4 #5;
        #print_array #1 arr;
        #flip arr arr;
        #print_array #1 arr;
        #len x arr;
        #F_RETURN x;
    #F_BODY_END;
#F_END;
```





## Спецификация ByteCode

- ADD dst src1 src2: сложение. Первый параметр - куда складывать значение на стек, второй - место на стеке, откуда взять первый параметр действия, третий - место второго параметра.
- SUB dst src1 src2: вычитание. Первый параметр - куда складывать значение на стек, второй - место на стеке, откуда взять первый параметр действия, третий - место второго параметра.
- MUL dst src1 src2: умножение. Первый параметр - куда складывать значение на стек, второй - место на стеке, откуда взять первый параметр действия, третий - место второго параметра.
- DIV dst src1 src2: деление. Первый параметр - куда складывать значение на стек, второй - место на стеке, откуда взять первый параметр действия, третий - место второго параметра.
- SET dst_off number: установка значения. Первый параметр - смещение на стеке, второй - число.
- RETURN name off: вызов функции с возвратом. Первый параметр - имя функции, второй - смещение на стеке.
- MOV dst_off src_off: вызов переноса значения. Первый параметр куда заносим, второй- откуда.
- JMP cmp_type jump_off: переход. Первый параметр - смещение назначения, второй - смещение источника, третий - тип сравнения, четвертый - смещение перехода.
