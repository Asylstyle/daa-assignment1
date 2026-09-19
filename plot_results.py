import csv
import math
from pathlib import Path

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

ROOT = Path(__file__).resolve().parent
OUTPUT = ROOT / "plots"
OUTPUT.mkdir(exist_ok=True)

with open(ROOT / "results.csv", newline="", encoding="utf-8-sig") as file:
    rows = list(csv.DictReader(file))

algorithms = ["MergeSort", "QuickSort", "QuickSelect"]
inputs = ["random", "sorted", "duplicates"]

colors = {
    "MergeSort": "tab:blue",
    "QuickSort": "tab:orange",
    "QuickSelect": "tab:green"
}

styles = {
    "random": "-",
    "sorted": "--",
    "duplicates": ":"
}


def get_rows(algorithm, input_type):
    selected = []

    for row in rows:
        if row["algorithm"] == algorithm and row["input"] == input_type:
            selected.append(row)

    return sorted(selected, key=lambda row: int(row["n"]))


def draw_graph(metric, title, ylabel, filename):
    fig, ax = plt.subplots(figsize=(10, 6))

    for algorithm in algorithms:
        for input_type in inputs:
            selected = get_rows(algorithm, input_type)
            sizes = [int(row["n"]) for row in selected]
            values = [float(row[metric]) for row in selected]

            ax.plot(
                sizes,
                values,
                color=colors[algorithm],
                linestyle=styles[input_type],
                marker="o",
                label=f"{algorithm} - {input_type}"
            )

    ax.set_xscale("log")

    if metric == "time_ms":
        ax.set_yscale("log")

    ax.set_title(title)
    ax.set_xlabel("Array size n (log scale)")
    ax.set_ylabel(ylabel)
    ax.grid(True, alpha=0.3)
    ax.legend(fontsize=8)

    fig.tight_layout()
    fig.savefig(OUTPUT / filename, dpi=200)
    plt.close(fig)


draw_graph(
    "time_ms",
    "Median running time vs array size",
    "Time in milliseconds (log scale)",
    "time.png"
)

draw_graph(
    "max_depth",
    "Maximum call depth vs array size",
    "Maximum call depth",
    "depth.png"
)

fig, axes = plt.subplots(1, 2, figsize=(13, 5))

for algorithm in algorithms:
    for input_type in inputs:
        selected = get_rows(algorithm, input_type)
        sizes = []
        ratios = []

        for row in selected:
            n = int(row["n"])
            comparisons = int(row["comparisons"])

            if algorithm == "QuickSelect":
                ratio = comparisons / n
            else:
                ratio = comparisons / (n * math.log2(n))

            sizes.append(n)
            ratios.append(ratio)

        ax = axes[1] if algorithm == "QuickSelect" else axes[0]

        ax.plot(
            sizes,
            ratios,
            color=colors[algorithm],
            linestyle=styles[input_type],
            marker="o",
            label=f"{algorithm} - {input_type}"
        )

axes[0].set_title("Sorting algorithms")
axes[0].set_ylabel("Comparisons / (n * log2(n))")

axes[1].set_title("QuickSelect")
axes[1].set_ylabel("Comparisons / n")

for ax in axes:
    ax.set_xscale("log")
    ax.set_xlabel("Array size n (log scale)")
    ax.grid(True, alpha=0.3)
    ax.legend(fontsize=8)

fig.tight_layout()
fig.savefig(OUTPUT / "ratio.png", dpi=200)
plt.close(fig)

print("Created plots/time.png, plots/depth.png, plots/ratio.png")