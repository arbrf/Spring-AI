$sessionId = "102"

while ($true) {
    $question = Read-Host "You"
    if ($question -eq "exit") { break }

    $url = "http://localhost:8083/api/chatclient?question=$question&sessionId=$sessionId"
    $response = Invoke-RestMethod -Uri $url -Method Get

    Write-Host "AI: $response.answer" -ForegroundColor Cyan
}
