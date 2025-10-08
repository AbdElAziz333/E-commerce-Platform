storage "file" {
    path = "/home/aziz/Desktop/vault/data" # Vault will store secrets here
}

listener "tcp" {
    address = "127.0.0.1:8200" # Vault listens here
    tls_disable = 1 # For local dev only, no TLS
}

ui = true # optional