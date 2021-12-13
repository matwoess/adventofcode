from pathlib import Path
import re
from collections import defaultdict


def pre_process_data(data: str) -> (list[str], list[str]):
    pattern = re.compile(r'(\w+ \w+ \w+ \w+ \w+ \w+ \w+ \w+ \w+ \w+) \| (\w+ \w+ \w+ \w+)')
    all_mappings, all_segments = [], []
    for line in data.splitlines():
        mappings, segments = pattern.match(line).groups()
        all_mappings.append(mappings.split(' '))
        all_segments.append(segments.split(' '))
    return all_mappings, all_segments


def part1(_: list[list[str]], segments: list[list[str]]) -> int:
    counter = 0
    unique_seg_lengths = [2, 4, 3, 7]
    for segments_list in segments:
        for s in segments_list:
            if len(s) in unique_seg_lengths:
                counter += 1
    return counter


def part2(mappings: list[list[str]], segments: list[list[str]]) -> int:
    unique_lengths = {
        1: 2,
        4: 4,
        7: 3,
        8: 7,
    }
    number_segments = {
        frozenset('abcefg'): '0',
        frozenset('cf'): '1',
        frozenset('acdeg'): '2',
        frozenset('acdfg'): '3',
        frozenset('bcdf'): '4',
        frozenset('abdfg'): '5',
        frozenset('abdefg'): '6',
        frozenset('acf'): '7',
        frozenset('abcdefg'): '8',
        frozenset('abcdfg'): '9',
    }

    def get_char_counts(string: str):
        counts = defaultdict(int)
        for ch in string:
            counts[ch] += 1
        return counts

    def chars_with_x_count(counts: dict, count: int):
        result = []
        for ch, cnt in counts.items():
            if cnt == count:
                result.append(ch)
        return result

    def get_char_mapping(patterns: list[str], char_counts: dict):
        segs1 = set([p for p in patterns if len(p) == unique_lengths[1]][0])
        segs4 = set([p for p in patterns if len(p) == unique_lengths[4]][0])
        segs7 = set([p for p in patterns if len(p) == unique_lengths[7]][0])
        segs8 = set([p for p in patterns if len(p) == unique_lengths[8]][0])
        a = segs7 - segs1
        assert len(a) == 1, 'too many "a"s'
        count8 = set(chars_with_x_count(char_counts, 8))  # only a and c appear 8 times
        c = count8 - a
        assert len(c) == 1, 'too many "c"s'
        f = segs7 - a - c
        assert len(f) == 1, 'too many "f"s'
        e = set(chars_with_x_count(char_counts, 4))
        assert len(e) == 1, 'too many "e"s'
        b = set(chars_with_x_count(char_counts, 6))
        assert len(b) == 1, 'too many "b"s'
        d = segs4 - c - b - f
        assert len(d) == 1, 'too many "d"s'
        g = segs8 - a - b - c - d - e - f
        assert len(g) == 1, 'too many "g"s'

        (a, b, c, d, e, f, g) = [tuple(x)[0] for x in [a, b, c, d, e, f, g]]
        return {a: 'a', b: 'b', c: 'c', d: 'd', e: 'e', f: 'f', g: 'g'}

    num_sum = 0
    for patterns, outputs in zip(mappings, segments):
        char_counts = get_char_counts(''.join(patterns))
        char_mapping = get_char_mapping(patterns, char_counts)

        num = ''
        for output in outputs:
            translated = {char_mapping[ch] for ch in output}
            num += number_segments[frozenset(translated)]
        num_sum += int(num)

    return num_sum


if __name__ == '__main__':
    data = Path('inputs/day08.txt').read_text(encoding='utf-8')
    mapping_data, segment_data = pre_process_data(data)
    answer1 = part1(mapping_data, segment_data)
    print('Answer1:', answer1)  # 440
    answer2 = part2(mapping_data, segment_data)
    print('Answer2:', answer2)  # 1046281
