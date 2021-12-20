from pathlib import Path
import numpy as np


def pre_process_data(data: str) -> np.ndarray:
    array = np.array([list(d) for d in data.splitlines()], dtype=int)
    return array


def print_dumbos(dumbos: np.ndarray):
    print()
    for row in dumbos:
        for col in row:
            if col > 9:
                print('>', end='')  # 2-digit numbers destroy format
            elif col == 0:
                print(f'\033[1m{0}\033[0m', end='')  # '\033[1m' - bold; '\033[0m' - reset
            else:
                print(col, end='')
        print()
    print()


def get_flashes_array(dumbos: np.ndarray, energy_capacity: int = 9) -> np.ndarray:
    return dumbos > energy_capacity


def get_flash_energy_propagation(flashes: np.ndarray) -> np.ndarray:
    energy = np.zeros_like(flashes, dtype=int)
    for r, row in enumerate(flashes):
        for c, col in enumerate(row):
            if col:
                from_r, to_r = max(0, r - 1), min(energy.shape[0] - 1, r + 1)
                from_c, to_c = max(0, c - 1), min(energy.shape[1] - 1, c + 1)
                energy[from_r:to_r + 1, from_c:to_c + 1] += 1
    return energy


def step(dumbos: np.ndarray) -> int:
    dumbos += 1
    flashes = get_flashes_array(dumbos)
    new_flashes = flashes
    while flashes.any():
        flash_energy = get_flash_energy_propagation(new_flashes)
        dumbos += flash_energy
        new_flashes = get_flashes_array(dumbos) ^ flashes
        if not new_flashes.any():
            break
        flashes |= new_flashes

    dumbos[flashes] = 0
    return flashes.sum()


def part1(dumbos: np.ndarray) -> int:
    dumbos = dumbos.copy()
    total_flashes = 0
    for _ in range(100):
        total_flashes += step(dumbos)
    print_dumbos(dumbos)
    return total_flashes


def part2(dumbos: np.ndarray) -> int:
    iteration = 1
    while True:
        if step(dumbos) == dumbos.size:
            print_dumbos(dumbos)
            return iteration
        iteration += 1


if __name__ == '__main__':
    dumbo_data = Path('inputs/day11.txt').read_text(encoding='utf-8')
    dumbo_array = pre_process_data(dumbo_data)
    answer1 = part1(dumbo_array.copy())
    print('Answer1:', answer1)  # 1652
    answer2 = part2(dumbo_array)
    print('Answer2:', answer2)  # 220
