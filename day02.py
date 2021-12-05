from dataclasses import dataclass
from pathlib import Path
import attr


@attr.s
class Command:
    kind: attr.ib(type=str)
    arg: attr.ib(type=int, converter=int)

    @classmethod
    def from_string(cls, line: str):
        (kind, arg) = line.split(' ')
        return Command(kind, arg)


@dataclass
class State:
    horizontal: int
    depth: int
    aim: int = None

    def apply_command(self, cmd: Command) -> 'State':
        if self.aim is None:
            if cmd.kind == "forward":
                self.horizontal = self.horizontal + cmd.arg
            if cmd.kind == "down":
                self.depth = self.depth + cmd.arg
            if cmd.kind == "up":
                self.depth = self.depth - cmd.arg
        else:
            if cmd.kind == "forward":
                self.horizontal = self.horizontal + cmd.arg
                self.depth = self.depth + self.aim * cmd.arg
            if cmd.kind == "down":
                self.aim = self.aim + cmd.arg
            if cmd.kind == "up":
                self.aim = self.aim - cmd.arg
        return self


def part1(cmds: list[Command]) -> int:
    state = State(0, 0)
    for cmd in cmds:
        state.apply_command(cmd)
    return state.depth * state.horizontal


def part2(cmds: list[Command]) -> int:
    state = State(0, 0, 0)
    for cmd in cmds:
        state.apply_command(cmd)
    return state.depth * state.horizontal


if __name__ == '__main__':
    data = Path('inputs/day02.txt').read_text(encoding='utf-8')
    split_data = data.splitlines()
    commands = [Command.from_string(line) for line in split_data]
    print('Answer 1:', part1(commands))  # 2019945
    print('Answer 2:', part2(commands))  # 1599311480
