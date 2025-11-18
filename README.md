<h1 align="center">💱 Conversor de Moedas</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-blue?logo=kotlin" alt="Kotlin Version"/>
  <img src="https://img.shields.io/badge/Android-21%2B-brightgreen?logo=android" alt="Android API"/>
  <img src="https://img.shields.io/badge/License-MIT-lightgrey" alt="License"/>
</p>

<h2>📱 Sobre o Projeto</h2>
<p>Aplicativo Android nativo para conversão de valores entre diferentes moedas utilizando taxas de câmbio em tempo real.</p>

<h2>✨ Funcionalidades</h2>
<ul>
  <li>✅ Conversão entre múltiplas moedas (BRL, USD, EUR, etc.)</li>
  <li>✅ Taxas de câmbio atualizadas via API</li>
  <li>✅ Interface intuitiva e responsiva</li>
  <li>✅ Cálculos em tempo real</li>
  <li>✅ Arquitetura moderna MVVM</li>
</ul>

<h2>🛠️ Tecnologias Utilizadas</h2>
<ul>
  <li><strong>Linguagem:</strong> Kotlin</li>
  <li><strong>Arquitetura:</strong> MVVM (Model-View-ViewModel)</li>
  <li><strong>Networking:</strong> Retrofit + GSON</li>
  <li><strong>Async:</strong> Coroutines</li>
  <li><strong>UI:</strong> ViewBinding, XML Layouts</li>
  <li><strong>API:</strong> HG Finance (ou outra API de cotações)</li>
</ul>

<h2>🎯 Como Usar</h2>
<ol>
  <li>Informe o valor que deseja converter</li>
  <li>Selecione a moeda de origem</li>
  <li>Selecione a moeda de destino</li>
  <li>Clique em "Converter"</li>
  <li>Visualize o resultado da conversão</li>
</ol>

<h2>🚀 Como Executar</h2>
<h3>Pré-requisitos</h3>
<ul>
  <li>Android Studio Arctic Fox ou superior</li>
  <li>SDK Android 21+</li>
  <li>Kotlin 1.9.0+</li>
</ul>

<h3>Instalação</h3>
<ol>
  <li>Clone o repositório:
    <pre><code>git clone https://github.com/greidimar/Desafio_DQR_Tech.git</code></pre>
  </li>
  <li>Abra o projeto no Android Studio</li>
  <li>Configure sua API key no arquivo <code>local.properties</code>:
    <pre><code>API_KEY=sua_chave_aqui</code></pre>
  </li>
  <li>Execute o app em um emulador ou dispositivo físico</li>
</ol>

<h2>🏗️ Estrutura do Projeto</h2>
<pre>
app/
├── src/main/
│   ├── java/com/example/conversor/
│   │   ├── ui/
│   │   │   ├── MainActivity.kt
│   │   │   └── viewmodel/
│   │   │       └── ConverterViewModel.kt
│   │   ├── data/
│   │   │   ├── api/
│   │   │   │   ├── CurrencyApi.kt
│   │   │   │   └── model/
│   │   │   │       └── CurrencyResponse.kt
│   │   │   └── repository/
│   │   │       └── CurrencyRepository.kt
│   │   └── di/
│   │       └── Module.kt
│   └── res/
│       ├── layout/
│       │   └── activity_main.xml
│       └── values/
│           └── strings.xml
</pre>

<h2>📄 Licença</h2>
<p>Este projeto está sob a licença MIT. Veja o arquivo <a href="LICENSE">LICENSE</a> para mais detalhes.</p>

<h2>👨‍💻 Autor</h2>
<p>Desenvolvido por <a href="https://github.com/greidimar">Greidimar</a></p>

<h2>📞 Contato</h2>
<p>
  <a href="https://github.com/greidimar">GitHub</a> •
  <a href="https://www.linkedin.com/in/seu-linkedin/">LinkedIn</a>
</p>
