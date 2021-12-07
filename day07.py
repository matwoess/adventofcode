from pathlib import Path
from typing import Callable


def linear_cost(candidate_pos: int, from_pos: int) -> int:
    return abs(candidate_pos - from_pos)


def gaussian_sum_cost(candidate_pos: int, from_pos: int) -> int:
    n = linear_cost(candidate_pos, from_pos)
    return n * (n + 1) // 2


def min_fuel_cost(positions: list[int], cost_fn: Callable) -> int:
    min_cost = None
    for candidate in range(min(positions), max(positions)):
        cost = sum(map(lambda pos: cost_fn(candidate, pos), positions))
        if min_cost is None or cost < min_cost:
            min_cost = cost
    return min_cost


def part1(positions: list[int]) -> int:
    return min_fuel_cost(positions, linear_cost)


def part2(positions: list[int]) -> int:
    return min_fuel_cost(positions, gaussian_sum_cost)


if __name__ == '__main__':
    data = Path('inputs/day07.txt').read_text(encoding='utf-8')
    position_data = [int(line) for line in data.split(',')]
    answer1 = part1(position_data)
    print('Answer1:', answer1)  # 342641
    answer2 = part2(position_data)
    print('Answer2:', answer2)  # 93006301
