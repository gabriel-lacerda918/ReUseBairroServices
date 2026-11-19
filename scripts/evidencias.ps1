# Uso (PowerShell, na raiz do projeto):
#   .\scripts\evidencias.ps1 testes   -> roda os testes automatizados e salva a saída
#   .\scripts\evidencias.ps1 api      -> (com a API rodando) chama os endpoints e salva as respostas
param([Parameter(Mandatory = $true)][ValidateSet('testes', 'api')][string]$modo)

$pasta = "evidencias"
New-Item -ItemType Directory -Force -Path $pasta | Out-Null

if ($modo -eq 'testes') {
    mvn test 2>&1 | Tee-Object -FilePath "$pasta/01-testes-automatizados.txt"
    if (Test-Path "target/surefire-reports") {
        Copy-Item "target/surefire-reports/*.txt" $pasta -Force
    }
    Write-Host "`nEvidências de testes salvas em '$pasta/'" -ForegroundColor Green
    exit
}

$base = "http://localhost:8080"
function Salvar($nome, $obj) { $obj | ConvertTo-Json -Depth 8 | Out-File -Encoding utf8 "$pasta/$nome.json" }
function Enviar($metodo, $url, $corpo) {
    $bytes = [System.Text.Encoding]::UTF8.GetBytes(($corpo | ConvertTo-Json))
    Invoke-RestMethod -Method $metodo -Uri $url -ContentType "application/json; charset=utf-8" -Body $bytes
}

Invoke-WebRequest "$base/v3/api-docs" -UseBasicParsing | Select-Object -ExpandProperty Content |
    Out-File -Encoding utf8 "$pasta/02-openapi.json"
Salvar "03-listar-itens" (Invoke-RestMethod "$base/api/itens")
Salvar "04-listar-usuarios" (Invoke-RestMethod "$base/api/usuarios")

$novo = Enviar Post "$base/api/itens" @{
    titulo = "Mesa de escritório"; descricao = "Boa"; categoria = "MOVEL"; bairro = "Centro";
    endereco = "Rua Teste, 10"; cep = "09000000"; anuncianteId = 1 }
Salvar "05-criar-item" $novo
Salvar "06-coletar-item" (Enviar Patch "$base/api/itens/$($novo.id)/coleta" @{ coletorId = 2 })
Salvar "07-filtrar-itens" (Invoke-RestMethod "$base/api/itens?categoria=MOVEL&status=COLETADO")

try { Enviar Post "$base/api/itens" @{ titulo = "" } | Out-Null }
catch { $_.ErrorDetails.Message | Out-File -Encoding utf8 "$pasta/08-erro-validacao-400.json" }
try { Invoke-RestMethod "$base/api/itens/9999" | Out-Null }
catch { $_.ErrorDetails.Message | Out-File -Encoding utf8 "$pasta/09-erro-nao-encontrado-404.json" }

Write-Host "Evidências da API salvas em '$pasta/'" -ForegroundColor Green
Write-Host "Falta apenas: print do Swagger UI (http://localhost:8080/swagger-ui.html) e do H2 Console."
