import re
from pathlib import Path
from typing import NamedTuple

import numpy as np


class Dot(NamedTuple):
    x: int
    y: int


class Fold(NamedTuple):
    axis: str
    at: int


def pre_process_data(input_string: str) -> tuple[np.ndarray, list[Fold]]:
    dots = []
    folds = []
    dots_pattern = re.compile(r'(\d+),(\d+)')
    fold_pattern = re.compile(r'fold along ([x|y])=(\d+)')
    for line in input_string.splitlines():
        match = dots_pattern.match(line)
        if match:
            x, y = match.groups()
            dots.append(Dot(int(x), int(y)))
            continue
        match = fold_pattern.match(line)
        if match:
            axis, at = match.groups()
            folds.append(Fold(axis, int(at)))

    max_x = max([dot.x for dot in dots])
    max_y = max([dot.y for dot in dots])
    dot_array = np.zeros(shape=(max_y + 1, max_x + 1), dtype=bool)
    for dot in dots:
        dot_array[dot.y, dot.x] = True
    return dot_array, folds


def fold_up(dots: np.ndarray, on_row: int):
    upper_part = dots[:on_row, :]
    lower_part = dots[on_row + 1:, :]
    upper_part |= lower_part[::-1, :]
    return upper_part


def fold_left(dots: np.ndarray, on_col: int):
    left_part = dots[:, :on_col]
    right_part = dots[:, on_col + 1:]
    left_part |= right_part[:, ::-1]
    return left_part


def do_folds(dots: np.ndarray, folds: list[Fold]) -> np.ndarray:
    for fold in folds:
        if fold.axis == 'y':
            dots = fold_up(dots, fold.at)
        if fold.axis == 'x':
            dots = fold_left(dots, fold.at)
    return dots


def part1(dots: np.ndarray, folds: list[Fold]) -> int:
    dots = do_folds(dots.copy(), folds[:1])
    return dots.sum()


def part2(dots: np.ndarray, folds: list[Fold]) -> np.ndarray:
    dots = do_folds(dots.copy(), folds)
    return dots


def print_array(array: np.ndarray):
    for row in array:
        for col in row:
            print('#' if col else ' ', end='')
        print()


if __name__ == '__main__':
    input_data = Path('inputs/day13.txt').read_text(encoding='utf-8')
    parsed_data = pre_process_data(input_data)
    answer1 = part1(*parsed_data)
    print('Answer1:', answer1)  # 818
    answer2 = part2(*parsed_data)
    print('Answer2:')
    print_array(answer2)  # LRGPRECB
