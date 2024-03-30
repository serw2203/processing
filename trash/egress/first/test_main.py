from unittest import TestCase

from main import check_divider
from main import check_int


class Test(TestCase):
    def test_check_int(self):
        self.assertEqual(check_int("1"), 1)

        self.assertEqual(check_int("100"), 100)

        self.assertRaises(RuntimeError, check_int, 0)

        self.assertRaises(RuntimeError, check_int, "0")

        self.assertRaises(ValueError, check_int, "a")

        self.assertRaises(ValueError, check_int, "")

        # self.assertRaises(ValueError, check_int, None)

    def test_check_divider(self):
        self.assertRaises(ZeroDivisionError, check_divider, 1, 0)

        self.assertRaises(ZeroDivisionError, check_divider, 0, 2)

        self.assertEqual(check_divider(1, 2), "\n'1' - является делителем '2'\n")

        self.assertEqual(check_divider(35, 7), "\n'7' - является делителем '35'\n")

        self.assertEqual(check_divider(34, 7),
                         "\nУвы! Числа '34' и '7' не соотносятся друг с другом как делимое и делитель")
