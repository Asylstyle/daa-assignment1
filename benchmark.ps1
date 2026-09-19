Set-Location $PSScriptRoot

mvn compile

if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

java -cp target/classes Benchmark
exit $LASTEXITCODE