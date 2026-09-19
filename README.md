# DAA Assignment 1

Java implementations of MergeSort, QuickSort and QuickSelect,
with metrics, JUnit 5 tests and a benchmark.

Repository: https://github.com/Asylstyle/daa-assignment1

## Requirements

- JDK 17 or newer
- Maven 3.9+
- Python 3 and matplotlib for generating plots
- PowerShell for benchmark.ps1

Java, Maven and Python must be available in PATH.

## Build and test

Run commands from the project root, where pom.xml is located.

```powershell
mvn clean test
```

The supplied suite contains 16 test methods.

## Run the benchmark on Windows

```powershell
powershell -ExecutionPolicy Bypass -File .\benchmark.ps1
```

This compiles the project and creates results.csv in the project root.
Running it again replaces the previous results.

Alternatively, run these commands in order:

```text
mvn compile
java -cp target/classes Benchmark
```

The benchmark measures three algorithms, three input types and four
sizes. Each case runs five times after warm-up.

The CSV has 36 data rows and the columns:

```text
algorithm,input,n,time_ms,comparisons,max_depth
```

Comparisons and depth come from the median-time run.
QuickSelect uses k = n / 2.

## Generate plots

```powershell
python -m pip install matplotlib
python plot_results.py
```

Outputs:
- plots/time.png
- plots/depth.png
- plots/ratio.png

Regenerating benchmark results requires regenerating plots and updating
the numerical discussion in REPORT.md.

## Project files

- src/main/java/Metrics.java: comparison, depth and time measurements
- src/main/java/MergeSort.java: merge sort with one buffer and cutoff 15
- src/main/java/QuickSort.java: random three-way partition and bounded stack
- src/main/java/QuickSelect.java: iterative selection using shared partition
- src/main/java/Benchmark.java: warm-up, measurements and CSV output
- src/test/java/: JUnit tests
- benchmark.ps1: benchmark launcher
- plot_results.py: graph generation
- results.csv: recorded measurements
- REPORT.md: complexity analysis and results

## Git workflow

Feature branches:
- feature/metrics
- feature/mergesort
- feature/quicksort
- feature/select
- feature/report

Completed changes are merged into main.
The final release is tagged v1.0.