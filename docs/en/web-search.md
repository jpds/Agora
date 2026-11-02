# Web Search

Open **Settings → Web Search** to choose a search backend and result count.

## Providers

Agora supports Brave, Kagi, Serper, Tavily, Parallel, SearXNG, and DuckDuckGo. Most require credentials; SearXNG requires the instance URL. DuckDuckGo is the default best-effort option and does not require an API key.

Web search is enabled by default. The default result count is 5 and can be set from 1–10.

## Parallel

Parallel is an AI-native search API: the query is sent as both an objective and a keyword query, and each result's markdown excerpts are joined into the description the model sees.

- **No key required.** Searches go to Parallel's public MCP endpoint (`https://search.parallel.ai/mcp`), which needs no credentials.
- **With a key.** The key from [platform.parallel.ai](https://platform.parallel.ai) switches to the billed REST endpoint, sends `mode: "fast"`, and caps each result's excerpts at 800 characters.

Results are capped to the configured count locally, so both paths honour the same result-count setting.

## Data flow

Queries are sent directly to the selected search provider. Returned snippets and links can be stored in the conversation as tool data and sent to the chat model during the agentic loop. Provider policies, quotas, and availability are external to Agora and can change.

See [Agentic Tools](tools.md) and [Privacy & Security](privacy.md).
