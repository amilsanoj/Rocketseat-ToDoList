# Projeto 3 – Deploy Contêinerizado no AKS com CI/CD

## Descrição

Este projeto implementa uma API REST de To-Do List desenvolvida em Java/Spring Boot, containerizada com Docker e deployada no Azure Kubernetes Service (AKS) com pipeline automático de CI/CD usando GitHub Actions.

## Arquitetura

### Componentes do Azure:
- **Azure Kubernetes Service (AKS)**: Orquestração dos containers
- **Azure Container Registry (ACR)**: Registry privado das imagens Docker
- **Load Balancer**: Exposição pública da aplicação

### Pipeline CI/CD:
1. Push no GitHub trigger o workflow
2. Build da imagem Docker no ACR
3. Deploy automático no cluster AKS
4. Verificação de health da aplicação