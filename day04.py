from pathlib import Path
import numpy as np


def pre_process_input(input_string: str) -> tuple[list[int], np.ndarray]:
    split_data = input_string.split('\n\n')
    draws = list(map(int, split_data[0].split(',')))
    board_strings = split_data[1:]
    boards_array = np.empty(shape=(len(board_strings), 5, 5), dtype=int)

    for i, board in enumerate(board_strings):
        lines = board.splitlines()
        for j, line in enumerate(lines):
            # split(None) (the default value) -> split on any whitespace, discard empty strings
            nums = [int(num) for num in line.split()]
            boards_array[i][j] = nums

    return draws, boards_array


def get_winner_indices(hits: np.ndarray, ignore_indices: list = None) -> list[int]:
    indices = []
    for i, board in enumerate(hits):
        if ignore_indices is not None and i in ignore_indices:
            continue
        for r in range(board.shape[0]):  # rows
            if all(board[r, :]):
                indices.append(i)
                continue
        for c in range(board.shape[1]):  # columns
            if all(board[:, c]):
                indices.append(i)
                continue
    return indices


def part1(draws: list[int], boards: np.ndarray) -> int:
    masks = np.zeros(boards.shape, dtype=bool)
    winner_draw = -1
    winner_idx = -1
    for draw in draws:
        masks |= boards == draw
        winner_indices = get_winner_indices(masks)
        if len(winner_indices) > 0:
            winner_idx = winner_indices[0]
            winner_draw = draw
            break

    return boards[winner_idx][~masks[winner_idx]].sum() * winner_draw


def part2(draws: list[int], boards: np.ndarray) -> int:
    masks = np.zeros(boards.shape, dtype=bool)
    all_winners = []
    winning_draw = -1
    winning_mask = None
    for draw in draws:
        masks |= boards == draw
        winner_indices = get_winner_indices(masks, all_winners)
        if len(winner_indices) > 0:
            all_winners += winner_indices
            winning_draw = draw
            winning_mask = masks[winner_indices[0]].copy()
            if len(all_winners) == boards.shape[0]:
                break
    return boards[all_winners[-1]][~winning_mask].sum() * winning_draw


if __name__ == '__main__':
    data = Path('inputs/day04.txt').read_text(encoding='utf-8')
    draw_data, board_data = pre_process_input(data)
    answer1 = part1(draw_data, board_data)
    print('Answer1:', answer1)  # 46920
    answer2 = part2(draw_data, board_data)
    print('Answer2:', answer2)  # 12635
