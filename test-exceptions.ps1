# Test Exception Handling for Spring MVC Shopping Website
Write-Host "=== Testing Exception Handling ===" -ForegroundColor Green

# Test 1: Resource Not Found (404)
Write-Host "`n1. Testing ResourceNotFoundException (404):" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories/999" -Method GET
    Write-Host "Unexpected success: $($response.StatusCode)"
} catch {
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $responseBody = $reader.ReadToEnd()
    Write-Host "Response Body:" -ForegroundColor Cyan
    Write-Host $responseBody
}

# Test 2: Validation Error (400) - Invalid data
Write-Host "`n2. Testing Validation Error (400):" -ForegroundColor Yellow
$invalidCategory = @{
    name = ""  # Invalid: empty name
    description = "Test"
} | ConvertTo-Json

$headers = @{
    'Content-Type' = 'application/json'
    'Authorization' = 'Bearer invalid-token'  # Will get 401 instead
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories" -Method POST -Body $invalidCategory -Headers $headers
    Write-Host "Unexpected success: $($response.StatusCode)"
} catch {
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $responseBody = $reader.ReadToEnd()
    Write-Host "Response Body:" -ForegroundColor Cyan
    Write-Host $responseBody
}

# Test 3: Unauthorized Access (401)
Write-Host "`n3. Testing Authentication Error (401):" -ForegroundColor Yellow
$validCategory = @{
    name = "Test Category"
    description = "Test Description"
} | ConvertTo-Json

$headers = @{
    'Content-Type' = 'application/json'
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories" -Method POST -Body $validCategory -Headers $headers
    Write-Host "Unexpected success: $($response.StatusCode)"
} catch {
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $responseBody = $reader.ReadToEnd()
    Write-Host "Response Body:" -ForegroundColor Cyan
    Write-Host $responseBody
}

# Test 4: Method Not Allowed (405)
Write-Host "`n4. Testing Method Not Allowed (405):" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories/1" -Method PATCH
    Write-Host "Unexpected success: $($response.StatusCode)"
} catch {
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $responseBody = $reader.ReadToEnd()
    Write-Host "Response Body:" -ForegroundColor Cyan
    Write-Host $responseBody
}

# Test 5: Successful Request (200)
Write-Host "`n5. Testing Successful Request (200):" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/categories" -Method GET
    Write-Host "Status Code: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "Content Sample: $($response.Content.Substring(0, [Math]::Min(100, $response.Content.Length)))..."
} catch {
    Write-Host "Unexpected error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Exception Handling Test Complete ===" -ForegroundColor Green 