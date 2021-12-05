from pathlib import Path


def window(levels: list[int], window_size=2) -> tuple:
    for i in range(len(levels) - (window_size - 1)):
        yield tuple(levels[i: i + window_size])


def part1(levels: list[int]) -> int:
    n_increasing = 0
    for win in window(levels, window_size=2):
        n_increasing += win[1] > win[0]
    return n_increasing


def part2(levels: list[int]) -> int:
    n_increasing = 0
    win_previous = None
    for win in window(levels, window_size=3):
        if win_previous is None:
            win_previous = win
            continue
        n_increasing += sum(win) > sum(win_previous)
        win_previous = win
    return n_increasing


if __name__ == '__main__':
    data = Path('inputs/day01.txt').read_text(encoding='utf-8')
    split_data = [int(line) for line in data.splitlines()]
    answer1 = part1(split_data)
    print('Answer1:', answer1)  # 1688
    answer2 = part2(split_data)
    print('Answer2:', answer2)  # 1728
