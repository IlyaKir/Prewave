# Prewave
## How to run (docker):
1. Add key for Prewave api in .env file: `PRIVATE_KEY=ilia.....`
2. Build docker image (from root folder of project): `docker build -t ilia_prewave .`
3. Run docker container `docker run --name api --env-file .env -p 8000:8000 -d ilia_prewave`
4. Use `docker logs api -f` to get logs
## How to run (locally):
1. Export key for Prewave api (unix terminal): `export PRIVATE_KEY=ilia...`
2. Run sbt (from root folder of project): `sbt run`

## API
1. Visit http://localhost:8000/graphql
2. Send json query via graphql api
```
query getTexts{
  getAlertMatches {
    countTerms
    countAllMatches
    matches {
      term {
        id
      }
      alerts {
        id
      }
    }
    allTerms {
      id
      text
      language
      keepOrder
    }
    allAlerts {
      id
      contents {
        text
        language
      }
    }
  }
}
```

Or short version with just Ids
```
query getIds {
  getAlertMatches {
    matches {
      term {
        id
      }
      alerts {
        id
      }
    }
  }
}
```

On the left side we write query and on the right side we get response:
<img width="1312" alt="graphql" src="https://github.com/IlyaKir/Prewave/assets/20299301/1a2eb64d-6e03-4be4-ae2a-7d2651d99f69">

Take a look at `DOCS` on the right side for details about query. Please pay attention that `allTerms` and `allAlerts` fields return all data which prewave api responses (this info is also in `DOCS` description).
<img width="1312" alt="docs" src="https://github.com/IlyaKir/Prewave/assets/20299301/9f13af68-0ef4-4da0-bc29-c2a323f564da">

## Ideas for imrovements:
1. If we have stream of alerts and want to react to them, then Akka Streams fits better
2. With current algorithm it's possible to find matches in parallel, but it will only work faster if we have large set of data (alerts). If we don't have so much data, it will cost more to create threads.
3. For testing API it's possible to generate code from GraphQL Schema and use it to create queries to api in Scala. 
