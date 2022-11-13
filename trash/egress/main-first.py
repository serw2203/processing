
print("Задача №1\nС клавиатуры вводится 2 целых числа. Проверить является ли одно из них делителем второго\n")


def input_int(welcome_message: str) -> int:
    i = ""
    while True:
        try:
            i = input(welcome_message)
            i = int(i)
            if i == 0:
                print("\n'0' - не годится)\n")
            else:
                return i
        except ValueError:
            print("\n'{}' - не является целым числом\n".format(i))


def check_divider(x: int, y: int) -> str:
    if (x % y) == 0:
        return "\n'{}' - является делителем '{}'\n".format(y, x)

    if (y % x) == 0:
        return "\n'{}' - является делителем '{}'\n".format(x, y)
    else:
        return "\nУвы! Числа '{}' и '{}' не соотносятся друг с другом как делимое и делитель".format(x, y)


i1 = input_int("Введите первое целое число : \n")
i2 = input_int("Введите второе целое число : \n")

print("\nОтвет : " + check_divider(i1, i2))
