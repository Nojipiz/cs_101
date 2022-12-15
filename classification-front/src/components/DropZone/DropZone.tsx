import { useCallback, useContext } from 'react';
import { CardImage } from 'react-bootstrap-icons';
import { useDropzone } from 'react-dropzone';
import {
  classificateImage,
  convertToBase64
} from '../../api/ClassificationRequest';
import { HomeContext } from '../HomePage/HomePage';
// import styles from './styles.module.css';
import styles from "./styles.module.css";
export default function DropZone() {
  const { setIsLoading, setOnError, setImage, setResult } =
    useContext(HomeContext);

  const classifyImage = async (file: File) => {
    const base64Image = await convertToBase64(file);
    if (base64Image != undefined) {
      setImage(base64Image);
      const result: number = await classificateImage(base64Image);
      setResult(result);
    }
  };

  const onDrop = useCallback(async (acceptedFiles: File[]) => {
    try {
      setIsLoading(true);
      await classifyImage(acceptedFiles[0]);
    } catch (e) {
      setOnError(true);
    } finally {
      setIsLoading(false);
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div className={styles.dropzone_container}>
      <div className={styles.dropzone} {...getRootProps()}>
        <CardImage size={120} />
        <input {...getInputProps()} />
        {isDragActive ? (
          <p className={styles.text}>Suelta la imagen aqui</p>
        ) : (
          <p className={styles.text}>
            Agarra y suelta la imagen que desees clasificar
          </p>
        )}
      </div>
    </div>
  );
}
